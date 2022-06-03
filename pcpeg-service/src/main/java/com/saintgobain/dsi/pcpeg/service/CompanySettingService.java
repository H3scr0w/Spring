package com.saintgobain.dsi.pcpeg.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.domain.PeDimTypeVersement;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParAccords;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSocieteId;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;
import com.saintgobain.dsi.pcpeg.domain.PeParVersement;
import com.saintgobain.dsi.pcpeg.domain.PeRefStatutFormulaire;
import com.saintgobain.dsi.pcpeg.dto.CompanySettingDTO;
import com.saintgobain.dsi.pcpeg.dto.CompanySettingValidationDTO;
import com.saintgobain.dsi.pcpeg.dto.ContactDTO;
import com.saintgobain.dsi.pcpeg.dto.DocumentDTO;
import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.dto.PaymentDTO;
import com.saintgobain.dsi.pcpeg.dto.TenantAccountDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.CampaignRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimTypeVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParAccordsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParFondsAutorisesSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefStatutFormulaireRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.specification.CompanySettingSpecification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompanySettingService {

    private final PeParFondsAutorisesSocieteRepository companyFundRepository;

    private final PeParAccordsRepository documentRepository;

    private final PeParVersementRepository perPaymentRepository;

    private final PeDimFondsRepository fundRepository;

    private final PeDimUtilisateursRepository usersRepository;

    private final PeParSocieteRepository peParSocieteRepository;

    private final PeDimTypeVersementRepository paymentTypeRepository;

    private final CampaignRepository campaignRepository;

    private final PeRefStatutFormulaireRepository formStatutRepository;

    private final PeDimAnneeService yearService;

    private final DocumentService documentService;

    private final AccessControl accessControl;

    @Transactional(readOnly = true)
    public CompanySettingDTO getCompanySettingsByIdAndPaymentType(Authentication authentication, Integer societeSid,
            Integer paymentTypeId)
            throws PcpegException {

        accessControl.checkAccessCompanyId(authentication, societeSid);

        PeDimUtilisateurs logUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);

        return buildCompanySetting(logUser, authentication, societeSid, paymentTypeId);

    }

    @Transactional
    public void validateCompanySettingsByIdAndPaymentType(Authentication authentication,
            Integer societeSid, Integer paymentTypeId, CompanySettingValidationDTO companySettingValidationDTO,
            MultipartFile[] file)
            throws PcpegException, IOException {

        accessControl.checkAccessCompanyId(authentication, societeSid);

        PeDimUtilisateurs logUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);

        Short currentYear = yearService.getCurrentYear();

        if (CollectionUtils.isEmpty(companySettingValidationDTO
                .getDocuments()) && (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET) || Objects
                        .equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET) || Objects
                                .equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION))) {

            deleteAllCompanySettingsDocuments(societeSid, currentYear, paymentTypeId);
            disableAllCompanySettings(logUser, societeSid, currentYear, paymentTypeId);

        } else {

            if (companySettingValidationDTO.getDefaultFund() == null && !Objects.equals(paymentTypeId,
                    Constants.PAYMENT_TYPE_CET)) {

                PeParVersement perPayment = disableAllCompanySettings(logUser, societeSid, currentYear, paymentTypeId);

                // validate agreements for interet or participation
                if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET)
                        || Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION)) {
                    validateDocuments(logUser, societeSid, currentYear, paymentTypeId, perPayment,
                            companySettingValidationDTO, file);
                }

            } else if ((Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET) || companySettingValidationDTO
                    .getDefaultFund() != null) && companySettingValidationDTO
                            .getPayment() != null) {

                PeParVersement perPayment = validateFund(logUser, societeSid, currentYear, paymentTypeId,
                        companySettingValidationDTO);

                // validate agreements for interet or participation or cet
                if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET)
                        || Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION)
                        || Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET)) {
                    validateDocuments(logUser, societeSid, currentYear, paymentTypeId, perPayment,
                            companySettingValidationDTO, file);
                }

            }

        }
        updateCompanyCampaignStatus(logUser, societeSid, currentYear, "2");

    }

    @Transactional
    public void validateCompanySettings(Authentication authentication, Integer societeSid) {
        accessControl.checkAccessCompanyId(authentication, societeSid);
        PeDimUtilisateurs logUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);

        Short currentYear = yearService.getCurrentYear();
        updateCompanyCampaignStatus(logUser, societeSid, currentYear, "4");
    }

    @Transactional
    private void updateCompanyCampaignStatus(PeDimUtilisateurs logUser, Integer societeSid, Short currentYear,
            String status) {
        Short formOne = Short.parseShort("1");
        PeParSuiviSocietesId id = PeParSuiviSocietesId
                .builder()
                .anneeId(currentYear)
                .societeSid(societeSid)
                .formulaireId(formOne)
                .build();
        PeParSuiviSocietes campaign = campaignRepository.findById(id).orElse(null);

        if (campaign != null) {

            Short statutUnderway = Short.parseShort(status);
            PeRefStatutFormulaire statutForm = formStatutRepository.findById(statutUnderway).orElse(null);
            if (statutForm != null) {
                campaign.setPeRefStatutFormulaire(statutForm);
                campaign.setLogDateMaj(new Date());
                campaign.setLogUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short.parseShort("-1"));
                campaignRepository.save(campaign);
            }
        }

    }

    @Transactional
    public PeParVersement buildPerPayment(PeDimUtilisateurs logUser, PeParSociete perCompany, Integer paymentTypeId) {

        PeDimTypeVersement paymentType = paymentTypeRepository.findById(
                paymentTypeId).orElseThrow(
                        () -> new EntityNotFoundException("Payment type not found for Id: " + paymentTypeId));

        PeParVersement perPayment = perPaymentRepository
                .findFirstByPeDimTypeVersement_TypeVersementSidAndPeParSociete_Id_SocieteSidAndPeParSociete_Id_AnneeId(
                        paymentType.getTypeVersementSid(), perCompany.getId().getSocieteSid(), perCompany.getId()
                                .getAnneeId()).orElse(
                                PeParVersement.builder()
                                        .flagVersement(false)
                                        .flagVersementInfra(false)
                                        .flagBlocPlie(false)
                                        .logDateMaj(new Date())
                                        .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                                                .parseShort("-1"))
                                        .peParSociete(perCompany)
                                        .peDimTypeVersement(paymentType)
                                        .build());

        return perPaymentRepository.save(perPayment);
    }

    @Transactional
    public PeParFondsAutorisesSociete buildCompanyFund(PeDimFonds fund, PeDimUtilisateurs logUser,
            PeParVersement perPayment) {
        PeParFondsAutorisesSocieteId id = PeParFondsAutorisesSocieteId
                .builder()
                .fondsSid(fund.getFondsSid())
                .parVersementSid(perPayment.getParVersementSid())
                .build();
        PeParFondsAutorisesSociete companyFund = PeParFondsAutorisesSociete
                .builder()
                .id(id)
                .logDateMaj(new Date())
                .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short.parseShort("-1"))
                .peDimFonds(fund)
                .peParVersement(perPayment)
                .flagActif(true)
                .flagFondsDefaut(false)
                .build();

        return companyFundRepository.save(companyFund);
    }

    private void deleteAllCompanySettingsDocuments(Integer societeSid, Short currentYear, Integer paymentTypeId)
            throws PcpegException {

        // delete all existing agreements for interet or participation or cet
        if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET)
                || Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION)
                || Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET)) {

            List<PeParAccords> agreements = documentRepository
                    .findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeIdAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
                            societeSid, currentYear, paymentTypeId);

            // get all agreements if not found for this year related to this company and payment
            if (CollectionUtils.isEmpty(agreements)) {
                agreements = documentRepository
                        .findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
                                societeSid, paymentTypeId);
            }

            if (CollectionUtils.isNotEmpty(agreements)) {
                try {
                    documentService.deleteAgreements(agreements);
                } catch (IOException e) {
                    log.warn("Agreements deletion : " + e.getMessage());
                }
            }
        }

    }

    private PeParVersement disableAllCompanySettings(PeDimUtilisateurs logUser, Integer societeSid, Short currentYear,
            Integer paymentTypeId) throws PcpegException {

        PeParSociete perCompany = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(societeSid, currentYear)
                .orElseThrow(
                        () -> new EntityNotFoundException("Company Id " + societeSid
                                + " not found for current campaign : "
                                + currentYear));

        PeDimTypeVersement paymentType = paymentTypeRepository.findById(paymentTypeId).orElseThrow(
                () -> new EntityNotFoundException("Payment type " + paymentTypeId + " not found"));

        // disable payment
        PeParVersement perPayment = perPaymentRepository
                .findFirstByPeDimTypeVersement_TypeVersementSidAndPeParSociete_Id_SocieteSidAndPeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear).orElse(PeParVersement
                                .builder()
                                .peDimTypeVersement(paymentType)
                                .peParSociete(perCompany)
                                .build());

        perPayment.setFlagVersement(false);
        perPayment.setFlagVersementInfra(false);
        perPayment.setFlagBlocPlie(false);
        perPayment.setLogDateMaj(new Date());
        perPayment.setLogUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                .parseShort("-1"));

        perPayment = perPaymentRepository.save(perPayment);

        // get settings from current campaign
        List<PeParFondsAutorisesSociete> companyFunds = companyFundRepository
                .findByPeParVersement_PeDimTypeVersement_TypeVersementSidAndPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear);

        if (CollectionUtils.isEmpty(companyFunds)) {

            // get old settings from previous campaign
            companyFunds = copyPreviousCompanySettings(companyFunds, logUser, societeSid, paymentTypeId, currentYear,
                    true);

            final PeParVersement paymentEntity = perPayment;

            // disable all company funds
            companyFunds = companyFunds.stream().map(cf -> {
                PeParFondsAutorisesSocieteId id = PeParFondsAutorisesSocieteId.builder()
                        .fondsSid(cf.getPeDimFonds().getFondsSid())
                        .parVersementSid(paymentEntity.getParVersementSid())
                        .build();

                return PeParFondsAutorisesSociete
                        .builder()
                        .id(id)
                        .flagActif(cf.getFlagActif())
                        .flagFondsDefaut(false)
                        .peDimFonds(cf.getPeDimFonds())
                        .peParVersement(paymentEntity)
                        .logDateMaj(new Date())
                        .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                                .parseShort("-1"))
                        .build();

            }).collect(Collectors.toList());

        }

        companyFundRepository.saveAll(companyFunds);

        return perPayment;

    }

    private void validateDocuments(PeDimUtilisateurs logUser, Integer societeSid, Short currentYear,
            Integer paymentTypeId, PeParVersement perPayment, CompanySettingValidationDTO companySettingValidationDTO,
            MultipartFile[] files) throws PcpegException, IOException {

        List<DocumentDTO> newDocuments = companySettingValidationDTO.getDocuments();

        newDocuments.stream().forEach(newDoc -> newDoc.setPaymentId(perPayment.getParVersementSid()));

        List<PeParAccords> agreements = documentRepository
                .findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeIdAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
                        societeSid, currentYear, paymentTypeId);

        // get all agreements if not found for this year related to this company and payment
        if (CollectionUtils.isEmpty(agreements)) {
            agreements = documentRepository
                    .findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
                            societeSid, paymentTypeId);
        }

        List<DocumentDTO> currentDocuments = agreements.stream().map(agreement -> {
            return DocumentDTO
                    .builder()
                    .documentId(agreement.getAccordSid())
                    .documentName(agreement.getNomDocument())
                    .documentType(agreement.getPeRefTypeAccord().getTypeAccordId())
                    .endDate(agreement.getDateFin())
                    .startDate(agreement.getDateDebut())
                    .paymentId(perPayment.getParVersementSid())
                    .build();
        }).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(currentDocuments)) {
            List<Integer> currentDocIds = currentDocuments.stream().map(DocumentDTO::getDocumentId).collect(Collectors
                    .toList());

            List<Integer> keepDocIds = newDocuments.stream().filter(newDoc -> newDoc.getDocumentId() != null && newDoc
                    .getDocumentId() != 0).map(DocumentDTO::getDocumentId).collect(Collectors
                            .toList());

            List<Integer> docIdsToDelete = currentDocIds.stream().filter(currentDocId -> !keepDocIds.contains(
                    currentDocId)).collect(Collectors
                            .toList());

            List<DocumentDTO> documentsToDelete = currentDocuments.stream().filter(currentDoc -> docIdsToDelete
                    .contains(currentDoc.getDocumentId()))
                    .collect(Collectors
                            .toList());

            if (CollectionUtils.isNotEmpty(documentsToDelete)) {
                // delete existing documents
                documentService.deleteDocuments(documentsToDelete);
            }

            // create new documents
            List<DocumentDTO> newDocumentsToCreate = newDocuments.stream().filter(newDoc -> newDoc
                    .getDocumentId() == null || newDoc.getDocumentId() == 0)
                    .collect(Collectors
                            .toList());

            if (CollectionUtils.isNotEmpty(newDocumentsToCreate)) {
                documentService.saveDocuments(logUser, files, newDocumentsToCreate);
            }

        } else {

            // create documents
            documentService.saveDocuments(logUser, files, newDocuments);
        }

    }

    private PeParVersement validateFund(PeDimUtilisateurs logUser, Integer societeSid, Short currentYear,
            Integer paymentTypeId,
            CompanySettingValidationDTO companySettingValidationDTO) throws PcpegException {
        PaymentDTO payment = companySettingValidationDTO.getPayment();

        PeParSociete perCompany = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(societeSid,
                currentYear)
                .orElseThrow(
                        () -> new EntityNotFoundException("Company Id " + societeSid
                                + " not found for current campaign : "
                                + currentYear));

        PeDimTypeVersement paymentType = paymentTypeRepository.findById(paymentTypeId).orElseThrow(
                () -> new EntityNotFoundException("Payment type " + paymentTypeId + " not found"));

        PeParVersement perPayment = perPaymentRepository
                .findFirstByPeDimTypeVersement_TypeVersementSidAndPeParSociete_Id_SocieteSidAndPeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear).orElse(PeParVersement.builder()
                                .peParSociete(perCompany)
                                .peDimTypeVersement(paymentType)
                                .build());

        // save payment
        perPayment.setFlagVersement(payment != null ? payment.getFlagVersement() : false);
        perPayment.setFlagVersementInfra(payment != null ? payment.getFlagVersementInfra() : false);
        perPayment.setFlagBlocPlie(payment != null ? payment.getFlagVersementBlocPlie() : false);
        perPayment.setLogDateMaj(new Date());
        perPayment.setLogUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                .parseShort("-1"));
        perPayment = perPaymentRepository.save(perPayment);

        // get all funds for current year
        List<PeParFondsAutorisesSociete> companyFunds = companyFundRepository
                .findByPeParVersement_PeDimTypeVersement_TypeVersementSidAndPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear);

        if (CollectionUtils.isEmpty(companyFunds)) {
            // get old settings from previous campaign
            companyFunds = copyPreviousCompanySettings(companyFunds, logUser, societeSid, paymentTypeId, currentYear,
                    true);

            final PeParVersement paymentEntity = perPayment;

            // create new settings if new setting
            companyFunds = companyFunds.stream().map(cf -> {
                PeParFondsAutorisesSocieteId id = PeParFondsAutorisesSocieteId.builder()
                        .fondsSid(cf.getPeDimFonds().getFondsSid())
                        .parVersementSid(paymentEntity.getParVersementSid())
                        .build();

                return PeParFondsAutorisesSociete
                        .builder()
                        .id(id)
                        .flagActif(cf.getFlagActif())
                        .flagFondsDefaut(cf.getFlagFondsDefaut())
                        .peDimFonds(cf.getPeDimFonds())
                        .peParVersement(paymentEntity)
                        .logDateMaj(new Date())
                        .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                                .parseShort("-1"))
                        .build();

            }).collect(Collectors.toList());

        }

        // build new company funds if any
        companyFunds = buildNewCompanyFunds(logUser, perCompany, perPayment, paymentType, companyFunds,
                companySettingValidationDTO);

        // process default fund except for cet payment
        if (!Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET)) {
            // check fund
            FundDTO fund = companySettingValidationDTO.getDefaultFund();
            PeDimFonds dimFund = fundRepository.findById(fund.getFundId()).orElseThrow(
                    () -> new EntityNotFoundException(
                            "Fund not found: "
                                    + fund.getFundLabel()));

            // get fund setting to set default
            PeParFondsAutorisesSociete companyFund = companyFunds.stream().filter(cf -> Objects.equals(cf
                    .getPeDimFonds()
                    .getFondsSid(), dimFund.getFondsSid()) && Objects.equals(cf.getPeParVersement()
                            .getPeDimTypeVersement()
                            .getTypeVersementSid(), paymentTypeId) && Objects.equals(cf.getPeParVersement()
                                    .getPeParSociete().getId().getSocieteSid(), societeSid)
                    && Objects.equals(cf.getPeParVersement().getPeParSociete().getId().getAnneeId(), currentYear))
                    .findFirst().orElse(buildNewCompanyFund(logUser, perCompany, perPayment, paymentType,
                            companySettingValidationDTO, dimFund));

            // set default fund
            companyFund.setFlagFondsDefaut(true);

            // set other funds as not default for payment
            companyFunds = companyFunds.stream().filter(cf -> !Objects.equals(cf
                    .getPeDimFonds()
                    .getFondsSid(), dimFund.getFondsSid())).map(cf -> {
                        cf.setFlagFondsDefaut(false);
                        return cf;
                    }).collect(Collectors.toList());

            // add default fund
            companyFunds.add(companyFund);

        }

        // update all funds
        companyFundRepository.saveAll(companyFunds);

        return perPayment;
    }

    private List<PeParFondsAutorisesSociete> buildNewCompanyFunds(
            PeDimUtilisateurs logUser, PeParSociete perCompany,
            PeParVersement perPayment, PeDimTypeVersement paymentType,
            List<PeParFondsAutorisesSociete> companyFunds,
            CompanySettingValidationDTO companySettingValidationDTO) {

        List<FundDTO> funds = companySettingValidationDTO.getFunds();

        if (CollectionUtils.isNotEmpty(funds)) {
            List<Short> currentFundIds = companyFunds.stream().map(cf -> cf.getPeDimFonds().getFondsSid()).collect(
                    Collectors.toList());
            List<Short> fundIds = funds.stream().map(nf -> nf.getFundId()).collect(
                    Collectors.toList());

            List<FundDTO> newFunds = funds.stream().filter(fund -> !currentFundIds
                    .contains(fund.getFundId())).collect(Collectors
                            .toList());

            List<PeParFondsAutorisesSociete> companyFundsToDelete = companyFunds.stream().filter(cf -> !fundIds
                    .contains(cf.getPeDimFonds().getFondsSid())).collect(Collectors.toList());

            // build new funds
            if (CollectionUtils.isNotEmpty(newFunds)) {
                for (FundDTO newFund : newFunds) {
                    PeDimFonds dimFund = fundRepository.findById(newFund.getFundId()).orElseThrow(
                            () -> new EntityNotFoundException(
                                    "Fund not found: "
                                            + newFund.getFundLabel()));
                    PeParFondsAutorisesSociete newCompanyFund = buildNewCompanyFund(logUser, perCompany, perPayment,
                            paymentType, companySettingValidationDTO, dimFund);
                    companyFunds.add(newCompanyFund);
                }
            }

            // delete old funds
            if (CollectionUtils.isNotEmpty(companyFundsToDelete)) {
                companyFundRepository.deleteInBatch(companyFundsToDelete);
            }
        }

        return companyFunds;
    }

    private PeParFondsAutorisesSociete buildNewCompanyFund(PeDimUtilisateurs logUser, PeParSociete perCompany,
            PeParVersement perPayment, PeDimTypeVersement paymentType,
            CompanySettingValidationDTO companySettingValidationDTO, PeDimFonds dimFund) {

        PeParFondsAutorisesSocieteId id = PeParFondsAutorisesSocieteId
                .builder()
                .fondsSid(dimFund.getFondsSid())
                .parVersementSid(perPayment.getParVersementSid())
                .build();
        PeParFondsAutorisesSociete companyFund = PeParFondsAutorisesSociete
                .builder()
                .id(id)
                .logDateMaj(new Date())
                .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short.parseShort("-1"))
                .peDimFonds(dimFund)
                .peParVersement(perPayment)
                .flagActif(true)
                .flagFondsDefaut(false)
                .build();

        return companyFund;
    }

    private List<PeParFondsAutorisesSociete> getCompanyFunds(PeDimUtilisateurs logUser, Authentication authentication,
            Integer societeSid,
            Integer paymentTypeId) throws PcpegException {
        Short currentYear = yearService.getCurrentYear();

        if (currentYear == null) {
            currentYear = yearService.getLastYear();
            if (currentYear == null) {
                throw new BadRequestException("No campaign is currently underway");
            }
        }

        Specification<PeParFondsAutorisesSociete> spec = generateSpecification(authentication, societeSid, currentYear,
                paymentTypeId);
        List<PeParFondsAutorisesSociete> companyFunds = companyFundRepository.findAll(spec);

        companyFunds = copyPreviousCompanySettings(companyFunds, logUser, societeSid, paymentTypeId, currentYear,
                false);

        return companyFunds;
    }

    private Specification<PeParFondsAutorisesSociete> generateSpecification(Authentication authentication,
            Integer societeSid, Short year, Integer paymentTypeId) {

        Specification<PeParFondsAutorisesSociete> spec = Specification.where(CompanySettingSpecification
                .companySpecification(societeSid));
        spec = spec.and(CompanySettingSpecification.yearSpecification(year));
        spec = spec.and(CompanySettingSpecification.paymentTypeSpecification(paymentTypeId));

        if (!accessControl.isAdmin(authentication)) {
            spec = spec.and(CompanySettingSpecification.companySgidSpecification(authentication.getPrincipal()
                    .toString()));
        }

        return spec;
    }

    private CompanySettingDTO buildCompanySetting(PeDimUtilisateurs logUser, Authentication authentication,
            Integer societeSid,
            Integer paymentTypeId) throws PcpegException {
        List<PeParFondsAutorisesSociete> companyFunds = getCompanyFunds(logUser, authentication, societeSid,
                paymentTypeId);

        CompanySettingDTO.CompanySettingDTOBuilder companySettingBuilder = CompanySettingDTO.builder();

        if (CollectionUtils.isNotEmpty(companyFunds)) {

            PeParFondsAutorisesSociete companyFund = companyFunds.stream().findFirst().orElse(null);

            if (companyFund != null && companyFund.getPeParVersement() != null) {

                PeParVersement perPayment = companyFund.getPeParVersement();
                PaymentDTO payment = buildPaymentSetting(companyFund, perPayment);
                companySettingBuilder.payment(payment);

                List<DocumentDTO> documents = buildDocumentSetting(perPayment);
                companySettingBuilder.documents(documents);

            }

            List<FundDTO> funds = buildFundSetting(companyFunds);
            companySettingBuilder.funds(funds);

        }

        return companySettingBuilder.build();
    }

    private PaymentDTO buildPaymentSetting(PeParFondsAutorisesSociete companyFund, PeParVersement perPayment) {

        PeDimTypeVersement paymentType = perPayment.getPeDimTypeVersement();

        PaymentDTO.PaymentDTOBuilder paymentBuilder = PaymentDTO.builder()
                .companyId(perPayment.getPeParSociete().getId().getSocieteSid())
                .year(perPayment.getPeParSociete().getId().getAnneeId())
                .flagVersement(perPayment.getFlagVersement())
                .flagVersementInfra(perPayment.getFlagVersementInfra())
                .flagVersementBlocPlie(perPayment.getFlagBlocPlie())
                .paymentId(perPayment.getParVersementSid());

        if (paymentType != null) {
            paymentBuilder.paymentType(paymentType.getTypeVersementSid());
        }

        return paymentBuilder.build();

    }

    private List<FundDTO> buildFundSetting(List<PeParFondsAutorisesSociete> companyFunds) {
        List<FundDTO> funds = new ArrayList<>();

        companyFunds.stream().forEach(companyFund -> {

            PeDimFonds dimFund = companyFund.getPeDimFonds();
            ContactDTO contact = ContactDTO.builder().id(dimFund.getPeDimContactFonds() != null ? dimFund
                    .getPeDimContactFonds().getContactId()
                    : null).build();

            FundDTO fund = FundDTO.builder()
                    .tenantAccount(dimFund.getPeRefTeneurCompte() != null ? TenantAccountDTO
                            .builder()
                            .teneurCompteId(dimFund.getPeRefTeneurCompte().getTeneurCompteId())
                            .teneurCompteLibelle(dimFund.getPeRefTeneurCompte().getTeneurCompteLibelle())
                            .build() : null)
                    .amundiCode(dimFund.getCodeFondsAmundi())
                    .fundId(dimFund.getFondsSid())
                    .fundGroupId(dimFund.getPeDimGrpFonds() != null ? dimFund.getPeDimGrpFonds().getGrpFondsId() : null)
                    .contact(contact.getId() != null ? contact : null)
                    .fundLabel(dimFund.getFondsLibelle())
                    .isDefault(companyFund.getFlagFondsDefaut())
                    .build();

            funds.add(fund);
        });

        return funds;
    }

    private List<DocumentDTO> buildDocumentSetting(PeParVersement perPayment) {

        List<DocumentDTO> documents = new ArrayList<>();
        Collection<PeParAccords> accords = perPayment.getPeParAccordses();

        // get all agreements if not found for this year related to this company and payment
        if (CollectionUtils.isEmpty(accords)) {
            accords = documentRepository
                    .findAllByPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeDimTypeVersement_TypeVersementSid(
                            perPayment.getPeParSociete().getId().getSocieteSid(), perPayment.getPeDimTypeVersement()
                                    .getTypeVersementSid());
        }

        if (CollectionUtils.isNotEmpty(accords)) {

            accords.stream().forEach(accord -> {
                DocumentDTO document = DocumentDTO.builder()
                        .documentId(accord.getAccordSid())
                        .documentName(accord.getNomDocument())
                        .documentType(accord.getPeRefTypeAccord() != null ? accord.getPeRefTypeAccord()
                                .getTypeAccordId() : null)
                        .endDate(accord.getDateFin())
                        .startDate(accord.getDateDebut())
                        .paymentId(perPayment.getParVersementSid())
                        .build();
                documents.add(document);
            });
        }

        return documents;
    }

    private List<PeParFondsAutorisesSociete> copyPreviousCompanySettings(List<PeParFondsAutorisesSociete> companyFunds,
            PeDimUtilisateurs logUser, Integer societeSid,
            Integer paymentTypeId,
            Short currentYear,
            boolean isValidated) throws PcpegException {
        if (CollectionUtils.isEmpty(companyFunds)) {
            // get company settings from existing previous campaign if no settings already set for current one
            Short previousYear = yearService.getPreviousYear();
            companyFunds = companyFundRepository
                    .findByPeParVersement_PeDimTypeVersement_TypeVersementSidAndPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeId(
                            paymentTypeId, societeSid, previousYear);

            // if still nothing in previous year so create for current year only for validation
            if (CollectionUtils.isEmpty(companyFunds) && isValidated) {
                // init company settings
                companyFunds = initCompanySettings(logUser, societeSid, paymentTypeId, currentYear);
            }
        }
        return companyFunds;
    }

    private List<PeParFondsAutorisesSociete> initCompanySettings(PeDimUtilisateurs logUser, Integer societeSid,
            Integer paymentTypeId,
            Short currentYear) throws PcpegException {
        List<PeParFondsAutorisesSociete> companyFunds = companyFundRepository
                .findByPeParVersement_PeDimTypeVersement_TypeVersementSidAndPeParVersement_PeParSociete_Id_SocieteSidAndPeParVersement_PeParSociete_Id_AnneeId(
                        paymentTypeId, societeSid, currentYear);

        if (CollectionUtils.isEmpty(companyFunds)) {
            companyFunds = new ArrayList<>();

            List<PeDimFonds> pegFunds = fundRepository.findDistinctByPeDimGrpFonds_GrpFondsIdStartsWithIgnoreCase(
                    Constants.FUND_PEG);
            List<PeDimFonds> divFunds = fundRepository.findDistinctByPeDimGrpFonds_GrpFondsIdStartsWithIgnoreCase(
                    Constants.FUND_DIVERS);
            List<PeDimFonds> defaultFunds = Stream.concat(pegFunds.stream(), divFunds.stream()).collect(Collectors
                    .toList());

            if (CollectionUtils.isNotEmpty(defaultFunds)) {

                PeParSociete perCompany = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(societeSid,
                        currentYear)
                        .orElseThrow(
                                () -> new EntityNotFoundException("Company Id " + societeSid
                                        + " not found for current campaign : "
                                        + currentYear));

                PeParVersement perPayment = null;
                if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION_SUPP)) {
                    perPayment = buildPerPayment(logUser, perCompany,
                            Constants.PAYMENT_TYPE_PARTICIPATION_SUPP);
                } else if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_PARTICIPATION)) {
                    perPayment = buildPerPayment(logUser, perCompany,
                            Constants.PAYMENT_TYPE_PARTICIPATION);
                } else if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET_SUPP)) {
                    perPayment = buildPerPayment(logUser, perCompany,
                            Constants.PAYMENT_TYPE_INTERET_SUPP);
                } else if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_INTERET)) {
                    perPayment = buildPerPayment(logUser, perCompany,
                            Constants.PAYMENT_TYPE_INTERET);
                } else if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET)) {
                    perPayment = buildPerPayment(logUser, perCompany,
                            Constants.PAYMENT_TYPE_CET);
                }

                if (perPayment == null) {
                    throw new BadRequestException("Unknown payment type: " + paymentTypeId);
                }

                if (Objects.equals(paymentTypeId, Constants.PAYMENT_TYPE_CET)) {
                    // add only peg funds for cet
                    for (PeDimFonds fund : pegFunds) {
                        companyFunds.add(buildCompanyFund(fund, logUser, perPayment));
                    }
                } else {
                    // add all default funds for other payment types
                    for (PeDimFonds fund : defaultFunds) {
                        companyFunds.add(buildCompanyFund(fund, logUser, perPayment));
                    }
                }

            }

        }
        return companyFunds;

    }

}
