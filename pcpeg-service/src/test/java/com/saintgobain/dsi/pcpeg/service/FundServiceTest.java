package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.domain.PeDimFonds;
import com.saintgobain.dsi.pcpeg.dto.FundDTO;
import com.saintgobain.dsi.pcpeg.repository.PeDimContactFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimGrpFondsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParFondsAutorisesSocieteRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefTeneurCompteRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class FundServiceTest {

    @Mock
    private PeDimFondsRepository fundRepository;

    @Mock
    private PeDimGrpFondsRepository fundGroupRepository;

    @Mock
    private PeDimContactFondsRepository fundContactRepository;

    @Mock
    private PeRefTeneurCompteRepository accountRepository;

    @Mock
    private PeParFondsAutorisesSocieteRepository companyFundRepository;

    @Mock
    private PeParVersementRepository perPaymentRepository;

    @Mock
    private PeDimUtilisateursRepository usersRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private PeDimAnneeService yearService;

    @Mock
    private AccessControl accessControl;

    private FundService fundService;

    private Page<PeDimFonds> results;

    private Pageable pageable;

    @Before
    public void setUp() throws Exception {
        PeDimFonds fund1 = PeDimFonds.builder().fondsSid(Short.parseShort("1")).build();
        PeDimFonds fund2 = PeDimFonds.builder().fondsSid(Short.parseShort("2")).build();

        List<PeDimFonds> funds = Arrays.asList(new PeDimFonds[] {
                fund1,
                fund2 });

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<PeDimFonds>(funds,
                pageable,
                funds.size());
        
        fundService = new FundService(fundRepository, fundGroupRepository, fundContactRepository, accountRepository,
                companyFundRepository, perPaymentRepository, usersRepository, accessControl, yearService);

    }

    @Test
    public void getAllFundsTest() throws Exception {

        // Given
        given(fundRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(results);

        // When
        Page<FundDTO> funds = fundService.getAllFunds(pageable, null, null, false);

        // Then
        assertThat(funds).isNotNull();
        assertThat(funds.getContent()).isNotNull();
        assertThat(funds.getContent().size()).isEqualTo(2);
    }
}
