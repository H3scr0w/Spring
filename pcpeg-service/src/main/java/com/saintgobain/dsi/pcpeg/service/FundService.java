package com.saintgobain.dsi.pcpeg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Objects;
import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.domain.PeDimContactFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimGrpFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSocieteId;
import com.saintgobain.dsi.pcpeg.domain.PeParVersement;
import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;
import com.saintgobain.dsi.pcpeg.dto.ContactDTO;
import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.dto.FundExcelDTO;
import com.saintgobain.dsi.pcpeg.dto.TenantAccountDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimContactFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimGrpFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParFondsAutorisesSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefTeneurCompteRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.service.utils.ExcelUtils;
import com.saintgobain.dsi.pcpeg.service.utils.FundExcelMapper;
import com.saintgobain.dsi.pcpeg.service.utils.FundMapper;
import com.saintgobain.dsi.pcpeg.service.utils.SortUtil;
import com.saintgobain.dsi.pcpeg.specification.FundSpecification;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class FundService {

    private final PeDimFondsRepository fundRepository;

    private final PeDimGrpFondsRepository fundGroupRepository;

    private final PeDimContactFondsRepository fundContactRepository;

    private final PeRefTeneurCompteRepository accountRepository;

    private final PeParFondsAutorisesSocieteRepository companyFundRepository;

    private final PeParVersementRepository perPaymentRepository;

    private final PeDimUtilisateursRepository usersRepository;

    private final AccessControl accessControl;

    private final PeDimAnneeService yearService;

    @Transactional(readOnly = true)
    public Page<FundDTO> getAllFunds(Pageable pageable, String label, List<String> groups, Boolean isActive) {
        Specification<PeDimFonds> spec = generateSpecification(label, groups, isActive);

        pageable = SortUtil.ignoreCase(pageable);
        pageable = SortUtil.andSort(pageable, "fondsSid");

        Page<PeDimFonds> results = fundRepository.findAll(spec, pageable);

        return mapListToDtos(results);
    }

    @Transactional
	public FundDTO createFund(FundDTO fundDTO) throws PcpegException {

		if (fundRepository.findById(fundDTO.getFundId()).isPresent()) {
			throw new BadRequestException("FUND_ALREADY_EXISTS");
		}

		PeDimFonds fund = new PeDimFonds();
		fund = processFund(fundDTO, fund, true);
		return new FundMapper().apply(fund);

	}

    @Transactional
    public FundDTO updateFund(Short fundId, FundDTO fundDTO) throws PcpegException {

        Boolean hasChanged = false;
        PeDimFonds fund = fundRepository.findById(fundId)
                .orElseThrow(() -> new EntityNotFoundException("Fund not found with id:" + fundId));

        if (!StringUtils.equalsIgnoreCase(fund.getCodeFondsAmundi(), fundDTO.getAmundiCode())
                || !StringUtils.equalsIgnoreCase(fund.getFondsLibelle(), fundDTO.getFundLabel())
                || (fund.getPeRefTeneurCompte() == null && fundDTO.getTenantAccount() != null
                      && (fundDTO.getTenantAccount().getTeneurCompteId() != null
                      || fundDTO.getTenantAccount().getTeneurCompteId() != 0))
                || (fund.getPeRefTeneurCompte() != null && fundDTO.getTenantAccount() != null
                      && !Objects.equal(fund.getPeRefTeneurCompte().getTeneurCompteId(), fundDTO.getTenantAccount().getTeneurCompteId()))) {
            hasChanged = true;
        }

        fund = processFund(fundDTO, fund, hasChanged);
        return new FundMapper().apply(fund);

	}

    @Transactional
    public FundDTO createOrUpdateFundSettings(Authentication authentication, Integer societeSid, Integer paymentTypeId,
            FundDTO fundDTO) throws PcpegException {

        accessControl.checkAccessCompanyId(authentication, societeSid);

        PeDimFonds fund = new PeDimFonds();
        if (fundDTO.getFundId() != null) {

            fund = fundRepository.findById(fundDTO.getFundId()).orElseThrow(() -> new EntityNotFoundException(
                    "Fund not found with Id: " + fundDTO.getFundId()));
        } else {
            fund = fundRepository.findDistinctByFondsLibelleContainingIgnoreCase(fundDTO.getFundLabel()).orElse(fund);
        }
        fund = processFund(fundDTO, fund, true);

        processFundSettings(authentication, societeSid, paymentTypeId, fund);

        return new FundMapper().apply(fund);
    }


    @Transactional(readOnly = true)
    public byte[] dowloadFunds(String label, List<String> groups) {
        byte[] data = new byte[] {};
        Page<FundDTO> funds = getAllFunds(Pageable.unpaged(), label, groups, false);
        List<FundDTO> fundsList = funds.getContent();
        if (CollectionUtils.isNotEmpty(fundsList)) {
            List<FundExcelDTO> fundExcelDtoList = fundsList.stream().map(new FundExcelMapper()).collect(Collectors
                    .toList());
            if (CollectionUtils.isNotEmpty(fundExcelDtoList)) {
                data = new ExcelUtils().writeToExcel(fundExcelDtoList, "Fonds").readAllBytes();
            }
        }
        return data;
    }

    private Specification<PeDimFonds> generateSpecification(String label, List<String> groups, Boolean isActive) {
        Specification<PeDimFonds> spec = Specification.where(null);

        if (StringUtils.isNotBlank(label)) {
            spec = spec.and(FundSpecification.labelSpecification(label));
        }

        if (CollectionUtils.isNotEmpty(groups)) {
            spec = spec.and(FundSpecification.groupsSpecification(groups));
        }

        if (BooleanUtils.isTrue(isActive)) {
            spec = spec.and(FundSpecification.fundActiveSpecification());
        }
        return spec;
    }

    private void processFundSettings(Authentication authentication, Integer societeSid, Integer paymentTypeId,
            PeDimFonds fund) throws PcpegException {
        Short currentYear = yearService.getCurrentYear();

        if (currentYear == null) {
            currentYear = yearService.getLastYear();
            if (currentYear == null) {
                throw new BadRequestException("No campaign is currently underway");
            }
        }

        PeParVersement perPayment = perPaymentRepository
                .findFirstByPeDimTypeVersement_TypeVersementSidAndPeParSociete_Id_SocieteSidAndPeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear).orElse(null);

        if (perPayment != null) {
            PeDimUtilisateurs logUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                    .getAuthentication().getPrincipal().toString()).orElse(null);

            PeParFondsAutorisesSocieteId id = PeParFondsAutorisesSocieteId
                    .builder()
                    .fondsSid(fund.getFondsSid())
                    .parVersementSid(perPayment.getParVersementSid())
                    .build();
            PeParFondsAutorisesSociete companyFund = companyFundRepository.findById(id).orElse(
                    new PeParFondsAutorisesSociete());
            companyFund.setFlagActif(true);
            companyFund.setLogDateMaj(new Date());
            companyFund.setLogUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                    .parseShort("-1"));
            companyFund.setPeDimFonds(fund);
            companyFund.setPeParVersement(perPayment);
            PeDimGrpFonds fundGroup = fund.getPeDimGrpFonds();
            companyFund.setFlagFondsDefaut(fundGroup != null ? !StringUtils.equalsIgnoreCase(fundGroup.getGrpFondsId(),
                    Constants.FUND_SPECIFIQUE) : false);

            companyFundRepository.save(companyFund);
        }

    }

    private Page<FundDTO> mapListToDtos(Page<PeDimFonds> funds) {
        if (funds != null) {
            return funds.map(new FundMapper());
        }
        return new PageImpl<>(new ArrayList<>());
    }

    private PeDimFonds processFund(FundDTO fundDTO, PeDimFonds fund, Boolean hasChanged) throws PcpegException {

        if (hasChanged && fundRepository
                .findDistinctByFondsLibelleIgnoreCaseAndCodeFondsAmundiIgnoreCaseAndPeRefTeneurCompte_TeneurCompteId(
                    fundDTO.getFundLabel().trim(), fundDTO.getAmundiCode(), fundDTO.getTenantAccount().getTeneurCompteId())
                .isPresent()) {
            throw new BadRequestException("Fund Already Exists with Label " + fundDTO.getFundLabel() + "Amundi Code "
                    + fundDTO.getAmundiCode() + "TenantAccount " + fundDTO.getTenantAccount().getTeneurCompteId());
		}

        fund.setFlagEnCoursCreation(fundDTO.getIsActive());
        fund.setCodeFondsAmundi(fundDTO.getAmundiCode());
        fund.setFondsId(fundDTO.getAmundiCode());
        fund.setFondsLibelle(fundDTO.getFundLabel().trim());

        // process fund group
        fund = processFundGroup(fundDTO, fund);

        // process contact
        ContactDTO contact = fundDTO.getContact();
        if (contact != null && !StringUtils.isAnyBlank(contact.getName(), contact.getEmail())) {
            fund = processFundContact(contact, fund);
        }

        // process account
        TenantAccountDTO account = fundDTO.getTenantAccount();
        if (account != null) {
            fund = processFundAccount(account, fund);
        }

        return fundRepository.save(fund);
    }

    private PeDimFonds processFundGroup(FundDTO fundDTO, PeDimFonds fund) throws PcpegException {

        PeDimGrpFonds peDimGrpFonds = fundGroupRepository.findByGrpFondsIdStartingWithIgnoreCase(fundDTO
                .getFundGroupId()).orElseThrow(
                () -> new EntityNotFoundException("Fund group not found with Id: " + fundDTO.getFundGroupId()));
        fund.setPeDimGrpFonds(peDimGrpFonds);
        return fund;
    }

    private PeDimFonds processFundContact(ContactDTO contact, PeDimFonds fund) throws PcpegException {
        PeDimContactFonds contactEntity = new PeDimContactFonds();
        if (contact.getId() != null) {
            contactEntity = fundContactRepository.findById(contact.getId()).orElseThrow(
                    () -> new EntityNotFoundException("Fund contact not found with Id: " + contact.getId()));
        } else if (StringUtils.isNoneBlank(contact.getName(), contact.getEmail())) {
            contactEntity = fundContactRepository.findFirstByNomContactIgnoreCaseAndEmailIgnoreCase(contact.getName()
                    .trim(), contact.getEmail().trim()).orElse(PeDimContactFonds
                            .builder()
                            .nomContact(contact.getName().trim())
                            .email(contact.getEmail().trim())
                            .build());
        } else if (StringUtils.isNotBlank(contact.getName())) {
            contactEntity = fundContactRepository.findFirstByNomContactIgnoreCase(contact.getName()
                    .trim()).orElse(PeDimContactFonds
                            .builder()
                            .nomContact(contact.getName().trim())
                            .build());
        } else if (StringUtils.isNotBlank(contact.getEmail())) {
            contactEntity = fundContactRepository.findFirstByEmailIgnoreCase(contact.getEmail()
                    .trim()).orElse(PeDimContactFonds
                            .builder()
                            .email(contact.getEmail().trim())
                            .build());
        }

        contactEntity.setTelephone(contact.getPhone().trim());
        contactEntity = fundContactRepository.save(contactEntity);
        fund.setPeDimContactFonds(contactEntity);
        return fund;
    }

    private PeDimFonds processFundAccount(TenantAccountDTO accountDTO, PeDimFonds fund) throws PcpegException {

        if ((accountDTO.getTeneurCompteId() == null) ||
                (accountDTO.getTeneurCompteId() == 0)) {
            fund.setPeRefTeneurCompte(null);
            return fund;
        }

        PeRefTeneurCompte account = accountRepository.findById(accountDTO.getTeneurCompteId()).orElseThrow(
                () -> new EntityNotFoundException("Fund account not found with Id: " + accountDTO
                        .getTeneurCompteId()));

        fund.setPeRefTeneurCompte(account);

        return fund;
    }

}
