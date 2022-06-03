package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissement;
import com.saintgobain.dsi.pcpeg.domain.PeDimEtablissementId;
import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.dto.FacilityDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.CompanyRepository;
import com.saintgobain.dsi.pcpeg.repository.FacilityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class FacilityServiceTest {

    @Mock
    private FacilityRepository facilityRepository;

    @Mock
    private  CompanyRepository companyRepository;

    @Mock
    private FacilityService facilityService;

    private Pageable pageable;

    private Page<PeDimEtablissement> results;

    private PeDimSociete company;

    @Before
    public void setUp() throws Exception {
        PeDimEtablissementId peDimEtablissementId1 = PeDimEtablissementId.builder().societeSid(1).facilityId("B4444").build();
        PeDimEtablissementId peDimEtablissementId2 = PeDimEtablissementId.builder().societeSid(2).facilityId("B4444").build();
        PeDimEtablissement peDimEtablissement1 = PeDimEtablissement.builder().id(peDimEtablissementId1).facilityLabel("Test").facilityShortLabel("(B4444)")
                .isActive("O").build();
        PeDimEtablissement peDimEtablissement2 = PeDimEtablissement.builder().id(peDimEtablissementId2).facilityLabel("Test1").facilityShortLabel("(B4444)")
                .isActive("N").build();

        List<PeDimEtablissement> peDimEtablissementList = Arrays.asList(new PeDimEtablissement[] {peDimEtablissement1,peDimEtablissement2});

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<PeDimEtablissement>(peDimEtablissementList,
                pageable,
                peDimEtablissementList.size());

        facilityService = new FacilityService(facilityRepository,companyRepository);

        company = PeDimSociete.builder().codeSif("25300").societeSid(1).build();
    }

    @Test
    public void getAllFacilitiesTest() throws PcpegException {
        // Given
        given(facilityRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(results);

        // When
        Page<FacilityDTO> facilities = facilityService.getAllFacilities(pageable, null, null, null, null);

        // Then
        assertThat(facilities).isNotNull();
        assertThat(facilities.getContent()).isNotNull();
        assertThat(facilities.getContent().size()).isEqualTo(2);
    }

    @Test
    public void createOrUpdateFacilityTest() throws  PcpegException  {
        FacilityDTO saveFacility = FacilityDTO.builder().facilityId("B4444").facilityLabel("test").codeSif("25300").isActive("O").build();
        PeDimEtablissement peDimEtablissement = new PeDimEtablissement();
        // Given
        given(companyRepository.findFirstByCodeSif("25300")).willReturn(Optional.ofNullable(company));
        given(facilityRepository.save(Mockito.any(PeDimEtablissement.class))).willReturn(peDimEtablissement);
        given(facilityRepository.findById_FacilityIdAndId_SocieteSid(any(), any())).willReturn(Optional.of(
                peDimEtablissement));

        // When
        PeDimEtablissement testfacility = facilityService.createOrUpdateFacility(
                saveFacility);

        // Then
        Assert.assertNotNull(testfacility);
    }
}
