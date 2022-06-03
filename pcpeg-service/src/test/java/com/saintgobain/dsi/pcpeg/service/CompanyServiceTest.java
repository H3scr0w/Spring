package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.dto.PeDimSocieteDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.CampaignRepository;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimTypeVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParFondsAutorisesSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParVersementRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class CompanyServiceTest {
    @Mock
    private CompanyRepository companyRepository;
    
    @Mock
    private  AccessControl accessControl;

    @Autowired
    private PcpegProperties properties;

    private Pageable pageable;

    @Mock
    private CompanyService companyService;

    @Mock
    private  CampaignService campaignService;

    @Mock
    private  PeDimAnneeRepository peDimAnneeRepository;
    
    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private  PeParSocieteService peParSocieteService;

    @Mock
    private PeDimAnneeService peDimAnneeService;

    private CompanySettingService companySettingService;
    @Mock
    private PeDimUtilisateursRepository usersRepository;

    @Mock
    private PeParFondsAutorisesSocieteRepository companyFundRepository;

    @Mock
    private PeDimTypeVersementRepository paymentTypeRepository;

    @Mock
    private PeParVersementRepository perPaymentRepository;

    @Mock
    private PeDimFondsRepository fundRepository;

    @Mock
    private PeParSocieteRepository peParSocieteRepository;

    private Page<PeDimSociete> results;

    private Optional<PeDimSociete> result;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {
        PeDimSociete company1 = PeDimSociete.builder().societeSid(1).build();
        PeDimSociete company2 = PeDimSociete.builder().societeSid(2).build();

        List<PeDimSociete> companies = Arrays.asList(new PeDimSociete[] { company1,
                company2 });

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<PeDimSociete>(companies,
                pageable,
                companies.size());

        result = Optional.ofNullable(PeDimSociete.builder().societeSid(1).build());

        companyService = new CompanyService(companyRepository, campaignRepository, peDimAnneeRepository,
                usersRepository, fundRepository, peParSocieteRepository, accessControl, campaignService,
                peParSocieteService, peDimAnneeService,
                companySettingService);


        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.addAll(Arrays.asList(new SimpleGrantedAuthority(properties.getGroup().getUsers())));
        authentication = new UsernamePasswordAuthenticationToken(
                "R1304520", null,
                grantedAuthorities);
    }

    @Test
    public void getAllCompaniesTest() throws PcpegException {

        // Given
        given(companyRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(results);

        // When
        Page<PeDimSocieteDTO> company1 = companyService.getAllCompanies(authentication, pageable);
        Page<PeDimSocieteDTO> company2 = companyService.getAllCompanies(authentication, pageable);

        // Then
        assertThat(company1).isNotNull();
        assertThat(company1.getContent()).isNotNull();
        assertThat(company1.getContent().size()).isEqualTo(2);

        assertThat(company2).isNotNull();
        assertThat(company2.getContent()).isNotNull();
        assertThat(company2.getContent().size()).isEqualTo(2);
    }

    @Test
    public void getCompanyTest() throws PcpegException {

        // Given
        given(companyRepository.findBySocieteSid(any(Integer.class))).willReturn(result);
        given(companyRepository.findBySocieteSidAndPeParSocietes_PeDimUtilisateurs_SgidEquals(any(Integer.class), any(
                String.class))).willReturn(result);
        given(accessControl.checkAccessCompanyId(any(), any())).willReturn(result.get());

        // When
        PeDimSocieteDTO testCompany = companyService.getCompanyById(authentication, Integer.valueOf(1));

        // Then
        assertThat(testCompany).isNotNull();

    }

    @Test
    public void createCompanyTest() throws PcpegException {
        PeDimSociete saveCompany = PeDimSociete.builder().societeSid(1).codeSif("0001").build();
        PeDimSocieteDTO saveCompanyDTO = PeDimSocieteDTO.builder().societeSid(1).codeSif("0001").build();
        Optional<PeDimAnnee> lastYear = Optional.of(PeDimAnnee.builder().anneeId(Short.parseShort("2020")).build());
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        // Given
        given(companyRepository.findBySocieteSid(300)).willReturn(result);
        given(companyRepository.save(Mockito.any(PeDimSociete.class))).willReturn(saveCompany);
        given(peDimAnneeRepository.findTop1ByOrderByAnneeIdDesc()).willReturn(lastYear);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn("N8488950");
        PeDimSociete testCompany = companyService.createCompany(saveCompanyDTO);

        // Then
        assertThat(testCompany).isNotNull();
        assertEquals(saveCompany.getCodeSif(), testCompany.getCodeSif());

    }

    @Test
    public void updateCompanyTest() throws PcpegException {

        Integer societeSid = 8;
        PeDimSociete updateCompany = PeDimSociete.builder().societeSid(8).codeSif("37000").build();
        PeDimSocieteDTO updateCompanyDTO = PeDimSocieteDTO.builder().societeSid(8).codeSif("37000").build();

        // Given
        given(companyRepository.findBySocieteSid(societeSid)).willReturn(result);
        given(companyRepository.findBySocieteSidAndPeParSocietes_PeDimUtilisateurs_SgidEquals(any(Integer.class), any(
                String.class))).willReturn(result);
        given(companyRepository.save(Mockito.any(PeDimSociete.class))).willReturn(updateCompany);
        given(accessControl.checkAccessCompanyId(any(), any())).willReturn(result.get());

        // When
        PeDimSociete testCompany = companyService.updateCompany(authentication, societeSid, updateCompanyDTO);

        // Then
        assertThat(testCompany).isNotNull();
        assertEquals(updateCompany.getCodeSif(), testCompany.getCodeSif());
    }
}
