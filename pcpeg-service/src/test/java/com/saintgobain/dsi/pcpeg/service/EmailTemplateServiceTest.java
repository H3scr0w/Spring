package com.saintgobain.dsi.pcpeg.service;

import com.saintgobain.dsi.pcpeg.PcpegApplication;
import com.saintgobain.dsi.pcpeg.domain.PeRefFormulaire;
import com.saintgobain.dsi.pcpeg.dto.EmailTemplateDTO;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeRefFormulaireRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PcpegApplication.class)
public class EmailTemplateServiceTest {

    @Mock
    private  EmailTemplateService emailTemplateService;

    @Mock
    private  PeRefFormulaireRepository peRefFormulaireRepository;


    private Optional<PeRefFormulaire> result;

    private Calendar calendar;


    @Before
    public void setUp() throws Exception {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 15);

        result = Optional.ofNullable(PeRefFormulaire.builder().formulaireId(Short.parseShort("1")).formulaireLibelle("Test").formulaireDateLimiteReponse(calendar.getTime())
                .mailInitial("test #PRENOM #NOM #ANNEE #URL_SITE #DATE").mailRelance("test1 #PRENOM #NOM #ANNEE #URL_SITE #DATE").objetInitial("subject #ANNEE")
                .objetRelance("Subject1 #ANNEE").build());

        emailTemplateService = new EmailTemplateService(peRefFormulaireRepository);


    }

    @Test
    public void getEmailTemplateByIdTest() throws PcpegException {

        // Given
        given(peRefFormulaireRepository.findById(any(Short.class))).willReturn(result);

        // When
        PeRefFormulaire testEmailTemplate = emailTemplateService.getEmailTemplateById( "1");

        // Then
        assertThat(testEmailTemplate).isNotNull();

    }

    @Test
    public void updateEmailTemplateTest() throws PcpegException {

        PeRefFormulaire updatePeRefFormulaire = PeRefFormulaire.builder().formulaireId(Short.parseShort("1")).formulaireLibelle("Test").formulaireDateLimiteReponse(calendar.getTime())
                .mailInitial("test #PRENOM #NOM #ANNEE #URL_SITE #DATE").mailRelance("test1 #PRENOM #NOM #ANNEE #URL_SITE #DATE").objetInitial("subject #ANNEE")
                .objetRelance("Subject1 #ANNEE").build();
        EmailTemplateDTO emailTemplateDTO = EmailTemplateDTO.builder() .mailInitial("test #PRENOM #NOM #ANNEE #URL_SITE #DATE").mailRelance("test1 #PRENOM #NOM #ANNEE #URL_SITE #DATE").objetInitial("subject #ANNEE")
                .objetRelance("Subject1 #ANNEE").formulaireDateLimiteReponse(calendar.getTime()).build();
        // Given
        given(peRefFormulaireRepository.findById(any(Short.class))).willReturn(result);
        given(peRefFormulaireRepository.save(Mockito.any(PeRefFormulaire.class))).willReturn(updatePeRefFormulaire);

        // When
        PeRefFormulaire testEmailTemplate = emailTemplateService.updateEmailTemplate( "1", emailTemplateDTO);

        // Then
        assertThat(testEmailTemplate).isNotNull();
    }
}
