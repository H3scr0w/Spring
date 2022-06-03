package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.client.directory.model.User;
import com.saintgobain.dsi.pcpeg.client.directory.service.UserService;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.config.PcpegPropertiesResolver;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.domain.PeParHabilitations;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.dto.AuthoritySettingDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.FacilityRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParHabilitationsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class AuthoritySettingServiceTest {

    @Mock
    private PeDimAnneeRepository peDimAnneeRepository;

    @Mock
    private PeParHabilitationsRepository peParHabilitationsRepository;

    @Mock
    private PeDimUtilisateursRepository usersRepository;

    @Mock
    private PeParSocieteRepository peParSocieteRepository;

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private AuthoritySettingService authoritySettingService;

    @Mock
    private AccessControl accessControl;

    @Mock
    private PcpegPropertiesResolver propertiesResolver;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;

    @Autowired
    private PcpegProperties properties;

    private Pageable pageable;

    private Page<PeParHabilitations> results;

    private PeDimAnnee currentYear;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {

        PeParHabilitations peParHabilitations1 = PeParHabilitations.builder().habilitationsSid(Short.parseShort("1"))
                .nom("test").prenom("test")
                .logUtilisateurId(Short.parseShort("1")).logDateMaj(new Date()).build();
        PeParHabilitations peParHabilitations2 = PeParHabilitations.builder().habilitationsSid(Short.parseShort("2"))
                .nom("test1").prenom("test1")
                .logUtilisateurId(Short.parseShort("1")).logDateMaj(new Date()).build();

        List<PeParHabilitations> peParHabilitationsList = Arrays.asList(new PeParHabilitations[] {
                peParHabilitations1,
                peParHabilitations2 });

        currentYear = PeDimAnnee.builder().anneeId(Short.parseShort("2019")).flagEnCours(true).build();

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<PeParHabilitations>(peParHabilitationsList,
                pageable,
                peParHabilitationsList.size());

        authoritySettingService = new AuthoritySettingService(peDimAnneeRepository, peParHabilitationsRepository,
                usersRepository, peParSocieteRepository, facilityRepository, accessControl, userService);

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.addAll(Arrays.asList(new SimpleGrantedAuthority(properties.getGroup().getUsers())));
        authentication = new UsernamePasswordAuthenticationToken(
                "N4300831", null,
                grantedAuthorities);
    }

    @Test
    public void getAllAuthoritySettingsTest() throws PcpegException {

        // Given
        given(peDimAnneeRepository.findByFlagEnCours(true)).willReturn(Optional.ofNullable(currentYear));
        given(peParHabilitationsRepository.findAll(any(Pageable.class))).willReturn(results);

        // When
        Page<AuthoritySettingDTO> authoritySettingDTO1 = authoritySettingService.getAllAuthoritySettings(authentication,
                1, pageable);
        Page<AuthoritySettingDTO> authoritySettingDTO2 = authoritySettingService.getAllAuthoritySettings(authentication,
                2, pageable);

        // Then
        assertThat(authoritySettingDTO1).isNotNull();
        assertThat(authoritySettingDTO1.getContent()).isNotNull();

        assertThat(authoritySettingDTO2).isNotNull();
        assertThat(authoritySettingDTO2.getContent()).isNotNull();
    }

    @Test
    public void createOrUpdateAuthoritySettingTest() throws PcpegException {
        AuthoritySettingDTO savePeParHabilitations = AuthoritySettingDTO
                .builder()
                .name("test")
                .firstname("test")
                .id(Short.parseShort("1"))
                .sgid("N8488950")
                .category(null)
                .build();
        Integer societeId = 1;
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        PeParHabilitations peParHabilitations = new PeParHabilitations();
        PeParSociete perCompany = new PeParSociete();
        // Given
        given(peDimAnneeRepository.findByFlagEnCours(true)).willReturn(Optional.ofNullable(currentYear));
        given(peParHabilitationsRepository.save(Mockito.any(PeParHabilitations.class))).willReturn(peParHabilitations);
        given(peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(any(), any())).willReturn(Optional.of(
                perCompany));
        given(userService.findUser(any())).willReturn(User.builder().build());

        // When
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(securityContext.getAuthentication().getPrincipal()).thenReturn("N8488950");
        AuthoritySettingDTO testPeParHabilitations = authoritySettingService.createOrUpdateAuthoritySetting(
                authentication, savePeParHabilitations, societeId);

        // Then
        Assert.assertNotNull(testPeParHabilitations);
    }

}
