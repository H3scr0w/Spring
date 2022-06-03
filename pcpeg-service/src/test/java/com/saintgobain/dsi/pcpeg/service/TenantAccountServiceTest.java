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
import com.saintgobain.dsi.pcpeg.domain.PeRefTeneurCompte;
import com.saintgobain.dsi.pcpeg.repository.PeRefTeneurCompteRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class TenantAccountServiceTest {

    @Mock
    private PeRefTeneurCompteRepository tenantAccountRepository;

    private TenantAccountService tenantAccountService;

    private Page<PeRefTeneurCompte> results;

    private Pageable pageable;

    @Before
    public void setUp() throws Exception {
        PeRefTeneurCompte tenantAccount1 = PeRefTeneurCompte.builder().teneurCompteId(Short.parseShort("1")).build();
        PeRefTeneurCompte tenantAccount2 = PeRefTeneurCompte.builder().teneurCompteId(Short.parseShort("2")).build();

        List<PeRefTeneurCompte> tenantAccounts = Arrays.asList(new PeRefTeneurCompte[] {
                tenantAccount1,
                tenantAccount2 });

        pageable = PageRequest.of(0, 20);

        results = new PageImpl<PeRefTeneurCompte>(tenantAccounts,
                pageable,
                tenantAccounts.size());

        tenantAccountService = new TenantAccountService(tenantAccountRepository);

    }

    @Test
    public void getAllFundsTest() throws Exception {

        // Given
        given(tenantAccountRepository.findAll(any(Specification.class), any(Pageable.class))).willReturn(results);

        // When
        Page<PeRefTeneurCompte> tenantAccounts = tenantAccountService.getAllTenantAccounts(pageable, null);

        // Then
        assertThat(tenantAccounts).isNotNull();
        assertThat(tenantAccounts.getContent()).isNotNull();
        assertThat(tenantAccounts.getContent().size()).isEqualTo(2);
    }
}
