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
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class YearServiceTest {

    @Mock
    private PeDimAnneeRepository peDimAnneeRepository;

    @Mock
    private YearService yearService;

    List<PeDimAnnee> years;

    @Before
    public void setUp() throws Exception {
        PeDimAnnee testYear1 = PeDimAnnee.builder().anneeId((short)2020).flagEnCours(true).build();
        PeDimAnnee testYear2 = PeDimAnnee.builder().anneeId((short)2019).flagEnCours(false).build();

        years = Arrays.asList(new PeDimAnnee[] { testYear1, testYear2 });

    }

    @Test
    public void getYears() throws PcpegException {

        // Given
        given(peDimAnneeRepository.findAll()).willReturn(years);

        // When
        List<PeDimAnnee> testYearsList = yearService.getYears();

        // Then
        assertThat(testYearsList).isNotNull();
    }

}
