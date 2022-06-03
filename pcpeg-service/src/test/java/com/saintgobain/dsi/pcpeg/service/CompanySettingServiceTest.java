package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.dto.CompanySettingDTO;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class CompanySettingServiceTest {

    @Mock
    private PeParFondsAutorisesSocieteRepository companyFundRepository;

    @Mock
    private PeParAccordsRepository documentRepository;

    @Mock
    private PeParVersementRepository perPaymentRepository;

    @Mock
    private PeDimFondsRepository fundRepository;

    @Mock
    private PeDimUtilisateursRepository usersRepository;

    @Mock
    private PeParSocieteRepository peParSocieteRepository;

    @Mock
    private PeDimTypeVersementRepository paymentTypeRepository;

    @Mock
    private CampaignRepository campaignRepository;

    @Mock
    private PeRefStatutFormulaireRepository formStatutRepository;

    @Mock
    private PeDimAnneeService yearService;

    @Mock
    private DocumentService documentService;

    @Mock
    private CompanyService companyService;

    @Mock
    private AccessControl accessControl;

    @Autowired
    private PcpegProperties properties;

    private Authentication authentication;

    private CompanySettingService companySettingService;

    private PeDimSociete company;

    @Before
    public void setUp() throws Exception {

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.addAll(Arrays.asList(new SimpleGrantedAuthority(properties.getGroup().getUsers())));
        authentication = new UsernamePasswordAuthenticationToken(
                "R1304520", null,
                grantedAuthorities);
        
        companySettingService = new CompanySettingService(companyFundRepository, documentRepository,
                perPaymentRepository, fundRepository, usersRepository, peParSocieteRepository, paymentTypeRepository,
                campaignRepository, formStatutRepository, yearService, documentService, accessControl);
    }

    @Test
    public void getCompanySettingsByIdAndPaymentTypeTest() throws Exception {

        // Given
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        given(accessControl.checkAccessCompanyId(any(Authentication.class), any(Integer.class))).willReturn(company);

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn("R1304520");
        CompanySettingDTO settings = companySettingService.getCompanySettingsByIdAndPaymentType(authentication, 52, 1);

        // Then
        assertThat(settings).isNotNull();
    }

}
