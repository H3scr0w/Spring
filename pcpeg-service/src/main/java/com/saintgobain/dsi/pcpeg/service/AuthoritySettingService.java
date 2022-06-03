package com.saintgobain.dsi.pcpeg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import com.saintgobain.dsi.pcpeg.client.directory.model.User;
import com.saintgobain.dsi.pcpeg.client.directory.service.UserService;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.dto.AuthoritySettingDTO;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.FacilityRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParHabilitationsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.service.utils.AuthoritySettingMapper;
import com.saintgobain.dsi.pcpeg.specification.AuthoritySettingSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthoritySettingService {
    private final PeDimAnneeRepository peDimAnneeRepository;
    private final PeParHabilitationsRepository peParHabilitationsRepository;
    private final PeDimUtilisateursRepository usersRepository;
    private final PeParSocieteRepository peParSocieteRepository;
    private final FacilityRepository facilityRepository;
    private final AccessControl accessControl;
    private final UserService userService;

    @Transactional
    public Page<AuthoritySettingDTO> getAllAuthoritySettings(final Authentication authentication, Integer societeSid,
            final Pageable pageable) throws PcpegException {
        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);
        Page<PeParHabilitations> results = null;

        if (currentYear == null) {
            currentYear = peDimAnneeRepository.findTop1ByOrderByAnneeIdDesc().orElse(null);
        }

        if (currentYear != null) {
            // get current year authorities
            results = getAuthoritiesByYear(authentication, pageable, currentYear.getAnneeId(), societeSid);

            if (results != null && CollectionUtils.isEmpty(results.getContent())) {
                // get last habilitation to get last year
                PeParHabilitations lastHabilitation = peParHabilitationsRepository
                        .findFirstByPeParSociete_Id_SocieteSidOrderByPeParSociete_Id_AnneeIdDesc(societeSid).orElse(
                                null);

                if (lastHabilitation != null && lastHabilitation.getPeParSociete() != null) {
                    // get all previous year authorities
                    results = getAuthoritiesByYear(authentication, Pageable.unpaged(), lastHabilitation
                            .getPeParSociete().getId()
                            .getAnneeId(), societeSid);

                    if (results != null && CollectionUtils.isNotEmpty(results.getContent())) {
                        // create all new authorities for current year
                        createNewAuthorities(results, societeSid, currentYear.getAnneeId());

                        // then get current year authorities
                        results = getAuthoritiesByYear(authentication, pageable, currentYear.getAnneeId(), societeSid);
                    }

                }
            }

            return mapListToDtos(results);
        } else {
            throw new BadRequestException("No campaign is opened");
        }
    }

    @Transactional
    public AuthoritySettingDTO createOrUpdateAuthoritySetting(final Authentication authentication,
            AuthoritySettingDTO authoritySettingDTO, Integer societeSid)
            throws PcpegException {
        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);
        PeDimUtilisateurs loggedUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);
        if (currentYear != null) {
            if (!accessControl.isAdmin(authentication)) {
                accessControl.checkAccessCompanyId(authentication, societeSid);
            }

            // check user infos
            User user = getUserInfos(authoritySettingDTO);

            // check facility infos
            FacilityDTO facility = authoritySettingDTO.getFacility();
            PeDimEtablissement peDimEtablissement = null;
            if (facility != null) {
                peDimEtablissement = facilityRepository.findById_FacilityIdAndId_SocieteSid(facility
                        .getFacilityId(), societeSid)
                        .orElseThrow(() -> new EntityNotFoundException("Facility not found for Id: " + facility
                                .getFacilityId() + " and company Id: " + societeSid));
            }

            if (authoritySettingDTO.getId() != null) {
                Optional<PeParHabilitations> peParHabilitation = peParHabilitationsRepository.findById(
                        authoritySettingDTO.getId());
                if (peParHabilitation.isPresent()) {
                    PeParHabilitations peParHabilitations = peParHabilitation.get();
                    peParHabilitations.setNom(user.getLastName());
                    peParHabilitations.setPrenom(user.getFirstName());
                    peParHabilitations.setSgid(user.getSgid());
                    peParHabilitations.setEmail(user.getMail());
                    peParHabilitations.setTelephone(user.getTelephoneNumber());
                    peParHabilitations.setLogUtilisateurId(loggedUser != null ? loggedUser.getUtilisateurId() : -1);
                    peParHabilitations.setLogDateMaj(new Date());
                    peParHabilitations.setCategory(authoritySettingDTO.getCategory());
                    peParHabilitations.setEtablissementId(peDimEtablissement != null && peDimEtablissement
                            .getId() != null ? peDimEtablissement.getId()
                            .getFacilityId()
                            : null);
                    peParHabilitations.setEtablissement(peDimEtablissement);
                    peParHabilitations = peParHabilitationsRepository.save(peParHabilitations);
                    return new AuthoritySettingMapper().apply(peParHabilitations);
                }
            }
            PeParSociete perCompany = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(societeSid,
                    currentYear.getAnneeId())
                    .orElseThrow(
                            () -> new EntityNotFoundException("Company Id " + societeSid
                                    + " not found for current campaign : "
                                    + currentYear));

            PeParHabilitations createdPeParHabilitation = new PeParHabilitations();
            createdPeParHabilitation.setNom(user.getLastName());
            createdPeParHabilitation.setPrenom(user.getFirstName());
            createdPeParHabilitation.setSgid(user.getSgid());
            createdPeParHabilitation.setEmail(user.getMail());
            createdPeParHabilitation.setTelephone(user.getTelephoneNumber());
            createdPeParHabilitation.setLogUtilisateurId(loggedUser != null ? loggedUser.getUtilisateurId() : -1);
            createdPeParHabilitation.setLogDateMaj(new Date());
            createdPeParHabilitation.setPeParSociete(perCompany);
            createdPeParHabilitation.setCategory(authoritySettingDTO.getCategory());
            createdPeParHabilitation.setEtablissementId(peDimEtablissement != null && peDimEtablissement
                    .getId() != null ? peDimEtablissement.getId()
                            .getFacilityId()
                            : null);
            createdPeParHabilitation.setEtablissement(peDimEtablissement);
            createdPeParHabilitation = peParHabilitationsRepository.save(createdPeParHabilitation);
            return new AuthoritySettingMapper().apply(createdPeParHabilitation);
        } else {
            throw new BadRequestException("No campaign is opened");
        }

    }

    @Transactional
    public void deleteAuthoritySetting(final Authentication authentication, Short authorityId, Integer societeSid)
            throws PcpegException {
        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);
        if (currentYear != null) {
            if (!accessControl.isAdmin(authentication)) {
                accessControl.checkAccessCompanyId(authentication, societeSid);
            }
            PeParHabilitations peParHabilitation = peParHabilitationsRepository.findById(authorityId).orElseThrow(
                    () -> new EntityNotFoundException("Authority not found for Id: " + authorityId));

            peParHabilitationsRepository.delete(peParHabilitation);

        } else {
            throw new BadRequestException("No campaign is opened");
        }

    }

    private Page<AuthoritySettingDTO> mapListToDtos(Page<PeParHabilitations> peParHabilitations) {
        if (peParHabilitations != null) {
            return peParHabilitations.map(new AuthoritySettingMapper());
        }
        return new PageImpl<>(new ArrayList<>());
    }

    private Page<PeParHabilitations> getAuthoritiesByYear(Authentication authentication, Pageable pageable, Short year,
            Integer societeSid) {
        Page<PeParHabilitations> results = null;
        Specification<PeParHabilitations> spec = Specification.where(AuthoritySettingSpecification
                .yearSpecification(year));
        spec = spec.and(AuthoritySettingSpecification.societeSpecification(societeSid));
        if (!accessControl.isAdmin(authentication)) {
            spec = spec.and(AuthoritySettingSpecification.companySgidSpecification(authentication.getPrincipal()
                    .toString()));
            results = peParHabilitationsRepository.findAll(spec, pageable);
        } else {
            results = peParHabilitationsRepository.findAll(spec, pageable);
        }
        return results;
    }

    private void createNewAuthorities(Page<PeParHabilitations> results, Integer societeSid, Short currentYear) {

        PeParSociete perCompany = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(societeSid, currentYear)
                .orElseThrow(
                        () -> new EntityNotFoundException("Company Id " + societeSid
                                + " not found for current campaign : "
                                + currentYear));

        PeDimUtilisateurs loggedUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);

        List<PeParHabilitations> newAuthorities = results.getContent().stream().map(auth -> {
            return PeParHabilitations.builder()
                    .email(auth.getEmail())
                    .etablissementId(auth.getEtablissementId())
                    .nom(auth.getNom())
                    .prenom(auth.getPrenom())
                    .sgid(auth.getSgid())
                    .logDateMaj(new Date())
                    .logUtilisateurId(loggedUser != null ? loggedUser.getUtilisateurId() : -1)
                    .telephone(auth.getTelephone())
                    .peParSociete(perCompany)
                    .category(auth.getCategory())
                    .build();
        }).collect(Collectors.toList());

        peParHabilitationsRepository.saveAll(newAuthorities);
    }

    private User getUserInfos(AuthoritySettingDTO authoritySettingDTO) {
        User user = null;
        try {
            user = userService.findUser(authoritySettingDTO.getSgid());
        } catch (HttpClientErrorException e) {
            throw new EntityNotFoundException("User " + authoritySettingDTO.getSgid() + " not found");
        }

        return user;
    }

}
