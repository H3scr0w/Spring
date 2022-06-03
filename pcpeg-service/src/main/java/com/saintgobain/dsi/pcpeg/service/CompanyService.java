package com.saintgobain.dsi.pcpeg.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSocieteId;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;
import com.saintgobain.dsi.pcpeg.domain.PeParVersement;
import com.saintgobain.dsi.pcpeg.dto.PeDimSocieteDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.CampaignRepository;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.service.utils.CompanyMapper;
import com.saintgobain.dsi.pcpeg.service.utils.ExcelUtils;
import com.saintgobain.dsi.pcpeg.service.utils.SortUtil;
import com.saintgobain.dsi.pcpeg.specification.CompanySpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private final CampaignRepository campaignRepository;

    private final PeDimAnneeRepository peDimAnneeRepository;

    private final PeDimUtilisateursRepository usersRepository;

    private final PeDimFondsRepository fundRepository;

    private final PeParSocieteRepository peParSocieteRepository;

    private final AccessControl accessControl;

    private final CampaignService campaignService;

    private final PeParSocieteService peParSocieteService;

    private final PeDimAnneeService peDimAnneeService;

    private final CompanySettingService companySettingService;

    @Transactional(readOnly = true)
    public Page<PeDimSocieteDTO> getAllCompanies(final Authentication authentication, Pageable pageable) {
        Page<PeDimSociete> results = null;
        Specification<PeDimSociete> spec = Specification.where(CompanySpecification.companyActiveSpecification());
        if (!accessControl.isAdmin(authentication)) {
            spec = spec.and(CompanySpecification.companySgidSpecification(authentication.getPrincipal().toString()));
            spec = spec.and(CompanySpecification.companyflagAdherenteSpecification());
        }
        pageable = SortUtil.ignoreCase(pageable);
        pageable = SortUtil.andSort(pageable, "societeSid");
        results = companyRepository.findAll(spec, pageable);
        return mapListToDtos(results);
    }

    @Transactional(readOnly = true)
    public Integer getAllFlagAdherentCompanies() {
        Specification<PeDimSociete> spec = Specification.where(CompanySpecification.companyActiveSpecification());
        spec = spec.and(CompanySpecification.companyflagAdherenteSpecification());
        List<PeDimSociete> results = companyRepository.findAll(spec);
        return CollectionUtils.isEmpty(results) ? 0 : results.size();
    }

    @Transactional(readOnly = true)
    public PeDimSocieteDTO getCompanyById(Authentication authentication, Integer societeSid)
            throws EntityNotFoundException {
        PeDimSociete result = accessControl.checkAccessCompanyId(authentication, societeSid);
        return mapPeDimSocieteToDtos(result);
    }

    @Transactional
    public void deleteCompany(Integer societeSid) throws EntityNotFoundException {
        PeDimSociete company = companyRepository.findBySocieteSid(societeSid)
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id:" + societeSid));
        company.setActive(false);
        company.setFlagAdherente(false);
        deleteCompanyToCampaign(company);
        companyRepository.save(company);
    }

    @Transactional
    public PeDimSociete createCompany(PeDimSocieteDTO peDimSocieteDTO) throws PcpegException {
        if (companyRepository.findBySocieteSid(peDimSocieteDTO.getSocieteSid()).isPresent()) {
            throw new BadRequestException("SOCIETESID_ALREADY_EXISTS");
        }

        PeDimSociete peDimSociete = new PeDimSociete();
        peDimSociete.setCodeSif(peDimSocieteDTO.getCodeSif());
        peDimSociete.setFlagAdherente(peDimSocieteDTO.getFlagAdherente());
        peDimSociete.setCodeAmundi(peDimSocieteDTO.getCodeAmundi());
        peDimSociete.setActive(true);
        peDimSociete.setSocieteLibelle(peDimSocieteDTO.getSocieteLibelle());
        peDimSociete.setCspId(peDimSocieteDTO.getCspId());
        PeDimSociete savedCompany = companyRepository.save(peDimSociete);
        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);

        if (currentYear != null && BooleanUtils.isTrue(peDimSociete.getFlagAdherente())) {
            // add company to current campaign
            addCompanyToCampaign(currentYear, savedCompany);
        }

        // always add default settings for last year
        addCompanySettings(savedCompany);

        return savedCompany;
    }

    @Transactional
    public PeDimSociete updateCompany(Authentication authentication, Integer societeSid,
            PeDimSocieteDTO peDimSocieteDTO) throws PcpegException {

        PeDimSociete peDimSociete = accessControl.checkAccessCompanyId(authentication, societeSid);

        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);

        Boolean adherentChanged = peDimSociete.getFlagAdherente() != peDimSocieteDTO.getFlagAdherente();
        peDimSociete.setCodeSif(peDimSocieteDTO.getCodeSif());
        peDimSociete.setFlagAdherente(peDimSocieteDTO.getFlagAdherente());
        peDimSociete.setCodeAmundi(peDimSocieteDTO.getCodeAmundi());
        peDimSociete.setSocieteLibelle(peDimSocieteDTO.getSocieteLibelle());
        peDimSociete.setCspId(peDimSocieteDTO.getCspId());
        PeDimSociete savedCompany = companyRepository.save(peDimSociete);

        if (adherentChanged) {
            if (currentYear != null && BooleanUtils.isTrue(peDimSociete.getFlagAdherente())) {
                addCompanyToCampaign(currentYear, savedCompany);
            } else if (BooleanUtils.isFalse(peDimSociete.getFlagAdherente())) {
                deleteCompanyToCampaign(savedCompany);
            }
        }

        return savedCompany;
    }

    @Transactional(readOnly = true)
    public byte[] downloadCompanies() {
        byte[] getBytes = new byte[] {};
        Specification<PeDimSociete> spec = Specification.where(CompanySpecification
                .companyActiveSpecification());
        List<PeDimSociete> peDimSocieteList = companyRepository.findAll(spec);
        if (CollectionUtils.isNotEmpty(peDimSocieteList)) {
            List<PeDimSocieteDTO> peDimSocieteDTOList = peDimSocieteList.stream().map(new CompanyMapper()).collect(
                    Collectors.toList());
            if (CollectionUtils.isNotEmpty(peDimSocieteDTOList)) {
                getBytes = new ExcelUtils().writeToExcel(peDimSocieteDTOList, "Companies").readAllBytes();
            }
        }

        return getBytes;
    }

    @Transactional
    public PeDimSociete addComment(Authentication authentication, Integer societeSid, String comments)
            throws EntityNotFoundException, BadRequestException {
        PeDimSociete peDimSociete = accessControl.checkAccessCompanyId(authentication, societeSid);
        peDimSociete.setComments(comments);
        return companyRepository.save(peDimSociete);
    }

    private Page<PeDimSocieteDTO> mapListToDtos(Page<PeDimSociete> companies) {
        if (companies != null) {
            return companies.map(new CompanyMapper());
        }
        return new PageImpl<>(new ArrayList<>());
    }

    private PeDimSocieteDTO mapPeDimSocieteToDtos(PeDimSociete company) {
        return new CompanyMapper().apply(company);
    }

    private void addCompanyToCampaign(PeDimAnnee currentYear, PeDimSociete savedCompany) throws PcpegException {
        List<PeParSociete> createdPeParSocietes = new ArrayList<>();
        createdPeParSocietes = peParSocieteService.createPeParSociete(currentYear.getAnneeId(), Arrays.asList(
                savedCompany), false);
        createdPeParSocietes.stream().forEach(newCompany -> {
            campaignService.setPeParSuiviSocietes(currentYear.getAnneeId(), newCompany);
        });
    }

    private void deleteCompanyToCampaign(PeDimSociete deletedCompany) {

        PeDimAnnee currentYear = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);

        if (currentYear != null) {
            Short formOne = Short.parseShort("1");
            PeParSuiviSocietesId id = PeParSuiviSocietesId
                    .builder()
                    .anneeId(currentYear.getAnneeId())
                    .formulaireId(formOne)
                    .societeSid(deletedCompany.getSocieteSid())
                    .build();
            PeParSuiviSocietes campaign = campaignRepository.findById(id).orElse(null);

            if (campaign != null) {
                campaignRepository.delete(campaign);
            }
        }

    }

    private void addCompanySettings(PeDimSociete savedCompany) throws PcpegException {

        // add company default settings for the last year in order to avoid
        // unexpected validation for current campaign
        Short previousYear = peDimAnneeService.getPreviousYear();

        PeDimUtilisateurs logUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);

        PeParSocieteId peParSocieteId = new PeParSocieteId();
        peParSocieteId.setAnneeId(previousYear);
        peParSocieteId.setSocieteSid(savedCompany.getSocieteSid());
        PeParSociete perCompany = peParSocieteRepository.findById(peParSocieteId).orElse(new PeParSociete());
        perCompany.setId(peParSocieteId);
        perCompany.setPeDimUtilisateurs(null);
        perCompany.setLogUtilisateurId(logUser != null ? logUser.getUtilisateurId() : -1);
        perCompany.setLogDateMaj(new Date());
        peParSocieteRepository.save(perCompany);

        List<PeDimFonds> pegFunds = fundRepository.findDistinctByPeDimGrpFonds_GrpFondsIdStartsWithIgnoreCase(
                Constants.FUND_PEG);
        List<PeDimFonds> divFunds = fundRepository.findDistinctByPeDimGrpFonds_GrpFondsIdStartsWithIgnoreCase(
                Constants.FUND_DIVERS);
        List<PeDimFonds> defaultFunds = Stream.concat(pegFunds.stream(), divFunds.stream()).collect(Collectors
                .toList());

        if (CollectionUtils.isNotEmpty(defaultFunds)) {

            PeParVersement participationSupp = companySettingService.buildPerPayment(logUser, perCompany,
                    Constants.PAYMENT_TYPE_PARTICIPATION_SUPP);
            PeParVersement participation = companySettingService.buildPerPayment(logUser, perCompany,
                    Constants.PAYMENT_TYPE_PARTICIPATION);
            PeParVersement interestSupp = companySettingService.buildPerPayment(logUser, perCompany,
                    Constants.PAYMENT_TYPE_INTERET_SUPP);
            PeParVersement interest = companySettingService.buildPerPayment(logUser, perCompany,
                    Constants.PAYMENT_TYPE_INTERET);
            PeParVersement cet = companySettingService.buildPerPayment(logUser, perCompany, Constants.PAYMENT_TYPE_CET);

            // add all default funds for other payment types
            for (PeDimFonds fund : defaultFunds) {
                companySettingService.buildCompanyFund(fund, logUser, participationSupp);
                companySettingService.buildCompanyFund(fund, logUser, participation);
                companySettingService.buildCompanyFund(fund, logUser, interestSupp);
                companySettingService.buildCompanyFund(fund, logUser, interest);
            }

            // add only peg funds for cet
            for (PeDimFonds fund : pegFunds) {
                companySettingService.buildCompanyFund(fund, logUser, cet);
            }

        }

    }

}
