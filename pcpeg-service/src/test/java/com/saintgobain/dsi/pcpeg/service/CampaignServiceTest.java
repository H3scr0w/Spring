package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.client.directory.service.GroupService;
import com.saintgobain.dsi.pcpeg.client.directory.service.UserService;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.domain.CampaignView;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietes;
import com.saintgobain.dsi.pcpeg.domain.PeParSuiviSocietesId;
import com.saintgobain.dsi.pcpeg.domain.PeRefFormulaire;
import com.saintgobain.dsi.pcpeg.domain.PeRefStatutFormulaire;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class CampaignServiceTest {

    @Mock
    private CampaignViewRepository campaignViewRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PeParSocieteRepository perCompanyRepository;

    @Mock
    private PeDimAnneeRepository peDimAnneeRepository;

    @Mock
    private PeDimUtilisateursRepository usersRepository;

    @Mock
    private PeParSuiviUtilisateursRepository perUsersRepository;

    @Mock
    private PeRefFormulaireRepository formRepository;

    @Mock
    private PeRefStatutFormulaireRepository statutFormRepository;

    @Mock
    private PeRefFormulaireRepository peRefFormulaireRepository;

    @Autowired
    private PcpegProperties properties;

    @Autowired
    private SpringTemplateEngine emailTemplateEngine;

    @Mock
    private GroupService groupService;

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    private Pageable pageable;

    @Mock
    private CampaignService campaignService;

    @Mock
    private  PeDimAnneeService peDimAnneeService;

    @Mock
    private  PeParSocieteService peParSocieteService;

    @Mock
    private AccessControl accessControl;

    private Page<CampaignView> results;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        CampaignView campaign1 = CampaignView.builder()
                .id(PeParSuiviSocietesId.builder().societeSid(1).build())
                .build();
        CampaignView campaign2 = CampaignView.builder()
                .id(PeParSuiviSocietesId.builder().societeSid(2).build())
                .build();

        List<CampaignView> campaigns = Arrays.asList(new CampaignView[] {
                campaign1,
                campaign2 });

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<CampaignView>(campaigns,
                pageable,
                campaigns.size());

        campaignService = new CampaignService(campaignViewRepository, campaignRepository, companyRepository,
                perCompanyRepository, peDimAnneeRepository, usersRepository, perUsersRepository, formRepository,
                statutFormRepository, peRefFormulaireRepository, groupService, userService, mailService, properties,
                emailTemplateEngine,
                peDimAnneeService, peParSocieteService, accessControl);

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.addAll(Arrays.asList(new SimpleGrantedAuthority(properties.getGroup().getUsers())));
        authentication = new UsernamePasswordAuthenticationToken(
                "R1304520", null,
                grantedAuthorities);
    }

    @Test
    public void getAllCampaignsTest() throws PcpegException {

        // Given
        given(campaignViewRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(results);

        // When
        Page<CampaignView> campaigns = campaignService.getAllCampaigns(authentication, pageable, null, null);

        // Then
        assertThat(campaigns).isNotNull();
        assertThat(campaigns.getContent()).isNotNull();
        assertThat(campaigns.getContent().size()).isEqualTo(2);
    }

   @Test
    public void createCampaignTest() throws PcpegException {
        PeParSuiviSocietesId savePeParSuiviSocietesId = PeParSuiviSocietesId.builder().anneeId((short) 1).formulaireId((short) 1).societeSid(1).build();
        PeRefStatutFormulaire savePeRefStatutFormulaire = PeRefStatutFormulaire.builder().statutId((short) 1).build();

        PeParSuiviSocietes saveCampaign = PeParSuiviSocietes.builder().id(savePeParSuiviSocietesId).flagChangement(true).logDateMaj(new Date()).
                peRefStatutFormulaire(savePeRefStatutFormulaire).logUtilisateurId((short) 1).build();

        Optional<PeRefFormulaire> form = Optional.of(PeRefFormulaire.builder().formulaireId(Short.parseShort(
                "1")).formulaireLibelle("Test").formulaireDateLimiteReponse(new Date())
                .mailInitial("test #PRENOM #NOM #ANNEE #URL_SITE #DATE").mailRelance(
                        "test1 #PRENOM #NOM #ANNEE #URL_SITE #DATE").objetInitial("subject #ANNEE")
                .objetRelance("Subject1 #ANNEE").build());
        // Given
        given(campaignRepository.save(Mockito.any(PeParSuiviSocietes.class))).willReturn(saveCampaign);
        given(peRefFormulaireRepository.findById(any(Short.class))).willReturn(form);

        // When
        List<PeParSuiviSocietes> testCampaign = campaignService.createCampaign("2020", true);

        // Then
        assertThat(testCampaign).isNotNull();
    }

}
