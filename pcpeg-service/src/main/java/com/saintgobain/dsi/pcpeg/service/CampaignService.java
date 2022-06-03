package com.saintgobain.dsi.pcpeg.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.saintgobain.dsi.pcpeg.client.directory.model.User;
import com.saintgobain.dsi.pcpeg.client.directory.service.GroupService;
import com.saintgobain.dsi.pcpeg.client.directory.service.UserService;
import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.domain.CampaignView;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviUtilisateursId;
import com.saintgobain.dsi.pcpeg.domain.PeRefFormulaire;
import com.saintgobain.dsi.pcpeg.domain.PeRefStatutFormulaire;
import com.saintgobain.dsi.pcpeg.dto.CampaignExcelDTO;
import com.saintgobain.dsi.pcpeg.dto.CampaignStatsDTO;
import com.saintgobain.dsi.pcpeg.dto.CorrespondantDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.CampaignRepository;
import com.saintgobain.dsi.pcpeg.repository.CampaignViewRepository;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSuiviUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefFormulaireRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefStatutFormulaireRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.service.utils.CampaignExcelMapper;
import com.saintgobain.dsi.pcpeg.service.utils.ExcelUtils;
import com.saintgobain.dsi.pcpeg.service.utils.SortUtil;
import com.saintgobain.dsi.pcpeg.specification.CampaignViewSpecification;
import com.saintgobain.dsi.pcpeg.specification.CompanySpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignViewRepository campaignViewRepository;
    private final CampaignRepository campaignRepository;
    private final CompanyRepository companyRepository;
    private final PeParSocieteRepository perCompanyRepository;
    private final PeDimAnneeRepository peDimAnneeRepository;
    private final PeDimUtilisateursRepository usersRepository;
    private final PeParSuiviUtilisateursRepository perUsersRepository;
    private final PeRefFormulaireRepository formRepository;
    private final PeRefStatutFormulaireRepository statutFormRepository;
    private final PeRefFormulaireRepository peRefFormulaireRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final MailService mailService;
    private final PcpegProperties properties;
    private final SpringTemplateEngine emailTemplateEngine;
    private final PeDimAnneeService peDimAnneeService;
    private final PeParSocieteService peParSocieteService;
    private final AccessControl accessControl;

    @Transactional(readOnly = true)
    public Page<CampaignView> getAllCampaigns(Authentication authentication, Pageable pageable, String year,
            String statutId)
            throws PcpegException {
        Specification<CampaignView> spec = Specification.where(CampaignViewSpecification.currentYearSpecification());

        if (StringUtils.isNotBlank(year)) {
            try {
                Short parsedYear = Short.parseShort(year);
                spec = Specification.where(CampaignViewSpecification.yearSpecification(parsedYear));
            } catch (NumberFormatException numberFormatException) {
                throw new BadRequestException("Year number bad format");
            }

        }

        if (StringUtils.isNotBlank(statutId)) {
            try {
                Short parsedStatutId = Short.parseShort(statutId);
                if (parsedStatutId == 2) {
                    spec = spec.and(CampaignViewSpecification.statusIdSpecification(parsedStatutId).or(
                            CampaignViewSpecification.statusIdSpecification(Short.parseShort("3"))));
                } else {
                    spec = spec.and(CampaignViewSpecification.statusIdSpecification(parsedStatutId));
                }
            } catch (NumberFormatException numberFormatException) {
                throw new BadRequestException("Statut number bad format");
            }
        }
        spec = spec.and(CampaignViewSpecification.formulaireOneSpecification());

        if (!accessControl.isAdmin(authentication)) {
            spec = spec.and(CampaignViewSpecification.correspondantSgidSpecification(authentication.getPrincipal()
                    .toString()));
        }

        pageable = SortUtil.ignoreCase(pageable);
        pageable = SortUtil.andSort(pageable, "id.societeSid");
        pageable = SortUtil.andSort(pageable, "id.anneeId");
        pageable = SortUtil.andSort(pageable, "id.formulaireId");

        Page<CampaignView> campaigns = campaignViewRepository.findAll(spec, pageable);
        return campaigns;
    }

    @Transactional(readOnly = true)
    public CampaignStatsDTO getCampaignStats(String year) throws PcpegException {

        Long companyAdherentCount = 0L;
        Long companyValidatedCount = 0L;
        Short formulaireId = Short.parseShort("1");
        Short statutId = Short.parseShort("4");

        if (StringUtils.isNotBlank(year)) {
            try {
                Short parsedYear = Short.parseShort(year);

                companyAdherentCount = campaignViewRepository.countById_FormulaireIdAndId_AnneeId(
                        formulaireId,
                        parsedYear);
                companyValidatedCount = campaignViewRepository
                        .countByStatutIdAndId_FormulaireIdAndId_AnneeId(statutId, formulaireId,
                                parsedYear);
            } catch (NumberFormatException numberFormatException) {
                throw new BadRequestException("Year number bad format");
            }

        } else {
            companyAdherentCount = campaignViewRepository.countByFlagEnCoursTrueAndId_FormulaireId(
                    formulaireId);
            companyValidatedCount = campaignViewRepository
                    .countByFlagEnCoursTrueAndStatutIdAndId_FormulaireId(statutId, formulaireId);
        }

        return CampaignStatsDTO.builder()
                .companyAdherentCount(companyAdherentCount)
                .companyValidatedCount(companyValidatedCount)
                .build();
    }

    @Transactional
    public void deleteCorrespondant(String societeId, String year) throws PcpegException {
        PeParSociete perCompany = checkCompanyCampaign(societeId, year);
        perCompany.setPeDimUtilisateurs(null);
        perCompanyRepository.save(perCompany);
    }

    @Transactional
    public void replaceCorrespondant(String societeId, String year, String sgid, Boolean isNotified)
            throws PcpegException, MessagingException {
        PeParSociete perCompany = checkCompanyCampaign(societeId, year);
        PeDimUtilisateurs user = usersRepository.findFirstBySgid(sgid).orElse(null);
        if (user == null) {
            groupService.addUserInGroup(sgid);
            User directoryUser = userService.findUser(sgid);
            user = PeDimUtilisateurs.builder()
                    .sgid(directoryUser.getSgid())
                    .email(directoryUser.getMail())
                    .nom(directoryUser.getLastName())
                    .prenom(directoryUser.getFirstName())
                    .telephone(directoryUser.getTelephoneNumber())
                    .flagActif(true)
                    .build();
            user = usersRepository.save(user);
        }
        perCompany.setPeDimUtilisateurs(user);
        perCompanyRepository.save(perCompany);

        if (BooleanUtils.isTrue(isNotified)) {
            final Context ctx = new Context(Locale.FRANCE);
            String template = this.emailTemplateEngine.process("invitation", ctx);
            template = template
                    .replace("#NAME", user.getNom())
                    .replace("#FIRSTNAME", user.getPrenom())
                    .replace("#URL_SITE", properties.getFrontendUrl());

            mailService.sendMail(new String[] {
                    user.getEmail() }, properties.getMail().getInvitationSubject(), template);
        }
    }

    @Transactional
    public void notifyCorrespondants(List<CorrespondantDTO> correspondants, Boolean isReminder) throws PcpegException,
            MessagingException {

        for (CorrespondantDTO correspondant : correspondants) {
            notifyCorrespondant(correspondant.getSocieteId(), correspondant.getYear(), correspondant.getUserId(),
                    isReminder);
        }

    }

    @Transactional
    public List<PeParSuiviSocietes> createCampaign(String year, Boolean copyPrevious) throws PcpegException {
        List<PeParSuiviSocietes> savedPeParSuiviSocietes = new ArrayList<>();
        if (StringUtils.isNotBlank(year)) {
            try {
                Short parsedYear = Short.parseShort(year);
                peDimAnneeService.createPeDimAnnee(parsedYear);
                List<PeParSociete> createdPeParSocietes = new ArrayList<>();
                Specification<PeDimSociete> spec = Specification.where(CompanySpecification
                        .companyflagAdherenteSpecification());
                spec = spec.and(CompanySpecification.companyActiveSpecification());
                List<PeDimSociete> companiesList = companyRepository.findAll(spec);
                createdPeParSocietes = peParSocieteService.createPeParSociete(parsedYear, companiesList, copyPrevious);
                createdPeParSocietes.stream().forEach(company -> {
                    savedPeParSuiviSocietes.add(setPeParSuiviSocietes(parsedYear, company));
                });
                Short formOne = Short.parseShort("1");
                PeRefFormulaire dbPeRefFormulaire = peRefFormulaireRepository.findById(formOne)
                        .orElseThrow(() -> new EntityNotFoundException("Template not found"));

                Date endDateResponse = dbPeRefFormulaire.getFormulaireDateLimiteReponse();
                LocalDate endLocalDateResponse = endDateResponse.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                if (!Objects.equals(endLocalDateResponse.getYear(), parsedYear.intValue())) {
                    endLocalDateResponse = endLocalDateResponse.withYear(parsedYear.intValue());
                    Date newEndDateResponse = Date
                            .from(endLocalDateResponse.atStartOfDay().atZone(ZoneId.systemDefault())
                                    .toInstant());
                    dbPeRefFormulaire.setFormulaireDateLimiteReponse(newEndDateResponse);
                    peRefFormulaireRepository.save(dbPeRefFormulaire);
                }

            } catch (NumberFormatException numberFormatException) {
                throw new BadRequestException("Year number bad format");
            }

        } else {
            throw new BadRequestException("Year cannot be empty");
        }
        return savedPeParSuiviSocietes;
    }

    @Transactional
    public PeParSuiviSocietes setPeParSuiviSocietes(Short parsedYear, PeParSociete company) {
        Short formOne = Short.parseShort("1");
        PeParSuiviSocietesId id = PeParSuiviSocietesId
                .builder()
                .societeSid(company.getId().getSocieteSid())
                .anneeId(parsedYear)
                .formulaireId(formOne)
                .build();
        PeParSuiviSocietes peParSuiviSocietes = campaignRepository.findById(id).orElse(new PeParSuiviSocietes());
        PeDimUtilisateurs adminUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);
        PeRefStatutFormulaire peRefStatutFormulaire = statutFormRepository.findById(Short.parseShort("1")).get();
        PeParSuiviSocietesId peParSuiviSocietesId = new PeParSuiviSocietesId();
        peParSuiviSocietesId.setAnneeId(parsedYear);
        peParSuiviSocietesId.setSocieteSid(company.getId().getSocieteSid());
        peParSuiviSocietesId.setFormulaireId(formOne);
        peParSuiviSocietes.setId(peParSuiviSocietesId);
        peParSuiviSocietes.setLogUtilisateurId(adminUser != null ? adminUser.getUtilisateurId() : Short.parseShort(
                "-1"));
        peParSuiviSocietes.setLogDateMaj(new Date());
        peParSuiviSocietes.setPeRefStatutFormulaire(peRefStatutFormulaire);
        peParSuiviSocietes.setFlagChangement(true);
        return campaignRepository.save(peParSuiviSocietes);
    }

    @Transactional(readOnly = true)
    public byte[] downloadCampaigns(Authentication authentication, String year, String statutId) throws PcpegException {
        byte[] data = new byte[] {};
        Page<CampaignView> campaigns = getAllCampaigns(authentication, Pageable.unpaged(), year, statutId);
        List<CampaignView> campaignsList = campaigns.getContent();
        if (CollectionUtils.isNotEmpty(campaignsList)) {
            List<CampaignExcelDTO> campaignExcelDtoList = campaignsList.stream().map(new CampaignExcelMapper()).collect(
                    Collectors.toList());
            if (CollectionUtils.isNotEmpty(campaignExcelDtoList)) {
                data = new ExcelUtils().writeToExcel(campaignExcelDtoList, "Campaigns").readAllBytes();
            }
        }

        return data;
    }

    @Transactional
    public PeDimAnnee closeCampaign(String year) throws PcpegException {
        PeDimAnnee peDimAnnee;
        Short parsedYear;
        try {
            parsedYear = Short.parseShort(year);
        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("Year number bad format");
        }
        peDimAnnee = peDimAnneeRepository.findByAnneeId(parsedYear)
                .orElseThrow(() -> new EntityNotFoundException("Year not found"));
        peDimAnnee.setFlagEnCours(false);
        return peDimAnneeRepository.save(peDimAnnee);
    }

    @Transactional
    public PeDimAnnee openCampaign(String year) throws PcpegException {
        PeDimAnnee peDimAnnee;
        Short parsedYear;
        try {
            parsedYear = Short.parseShort(year);
        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("Year number bad format");
        }
        peDimAnnee = peDimAnneeRepository.findByAnneeId(parsedYear)
                .orElseThrow(() -> new EntityNotFoundException("Year not found"));
        peDimAnnee.setFlagEnCours(true);
        return peDimAnneeRepository.save(peDimAnnee);
    }

    private PeParSociete checkCompanyCampaign(String societeId, String year) throws PcpegException {
        Integer parsedSocieteId = null;
        Short parsedYear = null;
        try {
            parsedSocieteId = Integer.parseInt(societeId);
            parsedYear = Short.parseShort(year);
        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("SocieteId or Year number bad format");
        }
        PeParSociete perCompany = perCompanyRepository.findFirstById_SocieteSidAndId_AnneeId(parsedSocieteId,
                parsedYear).orElseThrow(
                        () -> new EntityNotFoundException("Campaign not found for company: " + societeId + " for year: "
                                + year));
        return perCompany;
    }

    private PeDimUtilisateurs checkUser(String userId) throws PcpegException {
        Short parsedUserId = null;
        try {
            parsedUserId = Short.parseShort(userId);
        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("UserId number bad format");
        }
        PeDimUtilisateurs user = usersRepository.findById(parsedUserId).orElseThrow(
                () -> new EntityNotFoundException("User not found for id: " + userId));
        return user;
    }

    private void notifyCorrespondant(String societeId, String year, String userId, Boolean isReminder)
            throws PcpegException, MessagingException {
        PeParSociete perCompany = checkCompanyCampaign(societeId, year);
        PeDimUtilisateurs user = checkUser(userId);
        Short formulaireOne = Short.parseShort("1");
        PeParSuiviUtilisateursId id = PeParSuiviUtilisateursId
                .builder()
                .anneeId(perCompany.getId().getAnneeId())
                .formulaireId(formulaireOne)
                .utilisateurId(user.getUtilisateurId())
                .build();

        PeParSuiviUtilisateurs peUser = perUsersRepository.findById(id).orElse(PeParSuiviUtilisateurs
                .builder()
                .id(id)
                .flagActif(true)
                .flagEnvoieMail(false)
                .flagRelanceMail(false)
                .logDateMaj(new Date())
                .logUtilisateurId(user.getUtilisateurId())
                .build());

        PeRefFormulaire formOne = formRepository.findById(formulaireOne).orElseThrow(() -> new EntityNotFoundException(
                "Email templates for form One not found"));

        Date dateResponse = formOne.getFormulaireDateLimiteReponse();
        String formatDateResponse = "";
        if (dateResponse != null) {
            formatDateResponse = Constants.DATE_FORMAT.format(dateResponse);
        }

        if (BooleanUtils.isTrue(isReminder)) {

            if (!peUser.getFlagEnvoieMail()) {
                throw new BadRequestException("Initial email has not be sent to user : " + user.getEmail()
                        + " Send initial email instead");
            }

            // send reminder mail
            peUser.setDateDernierMail(new Date());
            peUser.setFlagRelanceMail(true);

            String template = formOne.getMailRelance()
                    .replace("#PRENOM", user.getPrenom())
                    .replace("#NOM", user.getNom())
                    .replace("#ANNEE", year)
                    .replace("#URL_SITE", properties.getFrontendUrl())
                    .replace("#DATE", formatDateResponse);

            mailService.sendMail(new String[] {
                    user.getEmail() }, formOne.getObjetRelance().replace("#ANNEE", year), template);

        } else {

            // send email only if first time
            if (!peUser.getFlagEnvoieMail()) {
                peUser.setDateDernierMail(new Date());
                peUser.setFlagEnvoieMail(true);

                String template = formOne.getMailInitial()
                        .replace("#PRENOM", user.getPrenom())
                        .replace("#NOM", user.getNom())
                        .replace("#ANNEE", year)
                        .replace("#URL_SITE", properties.getFrontendUrl())
                        .replace("#DATE", formatDateResponse);

                mailService.sendMail(new String[] {
                        user.getEmail() }, formOne.getObjetInitial().replace("#ANNEE", year), template);
            } else {
                throw new BadRequestException("Initial email has already be sent to user : " + user.getEmail()
                        + " Send reminder email instead");
            }
        }

        perUsersRepository.save(peUser);

    }

}
