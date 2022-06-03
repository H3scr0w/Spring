package com.saintgobain.dsi.pcpeg.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.domain.PeDimCsp;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimCspRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class CspServiceTest {

    @Mock
    private PeDimCspRepository peDimCspRepository;

    @Mock
    private CspService cspService;

    List<PeDimCsp> results;

    @Before
    public void setUp() throws Exception {
        PeDimCsp testCsp1 = PeDimCsp.builder().cspId("CON").isActive(true).build();
        PeDimCsp testCsp2 = PeDimCsp.builder().cspId("HTO").isActive(false).build();

        results = Arrays.asList(new PeDimCsp[] { testCsp1, testCsp2 });

    }

    @Test
    public void getAllCsp() throws PcpegException {

        // Given
        given(peDimCspRepository.findAll()).willReturn(results);

        // When
        List<PeDimCsp> testCspList = cspService.getAllCsp();

        // Then
        assertThat(testCspList).isNotNull();
    }

}