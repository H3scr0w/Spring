package com.saintgobain.dsi.pcpeg.service;

import com.saintgobain.dsi.pcpeg.domain.PeRefFormulaire;
import com.saintgobain.dsi.pcpeg.dto.EmailTemplateDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeRefFormulaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailTemplateService {
    private final PeRefFormulaireRepository peRefFormulaireRepository;

    @Transactional(readOnly = true)
    public PeRefFormulaire getEmailTemplateById(String formulaireId)
            throws PcpegException {
        try {
            Short parsedId = Short.parseShort(formulaireId);
            return peRefFormulaireRepository.findById(parsedId)
                    .orElseThrow(() -> new EntityNotFoundException("Template not found"));
        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("Id number bad format");
        }
    }

    @Transactional
    public PeRefFormulaire updateEmailTemplate(String formulaireId,
                                                  EmailTemplateDTO emailTemplateDTO) throws PcpegException {

        try {
            Short parsedId = Short.parseShort(formulaireId);
                PeRefFormulaire dbPeRefFormulaire = peRefFormulaireRepository.findById(parsedId)
                        .orElseThrow(() -> new EntityNotFoundException("Template not found"));
            List<String> matches = Arrays.asList("#PRENOM","#NOM","#ANNEE","#URL_SITE","#DATE");

            if(!matches.stream().allMatch(emailTemplateDTO.getMailRelance()::contains)) {
                throw new BadRequestException("In reminder email template one of the following pattern is missing: #PRENOM, #NOM ,#ANNEE, #URL_SITE, #DATE");
            }

            if(!matches.stream().allMatch(emailTemplateDTO.getMailInitial()::contains)) {
                throw new BadRequestException("In initial email template one of the following pattern is missing: #PRENOM, #NOM ,#ANNEE, #URL_SITE, #DATE");
            }

            if(!emailTemplateDTO.getObjetInitial().contains("#ANNEE")) {
                throw new BadRequestException("In initial object pattern is missing: #ANNEE");
            }

            if(!emailTemplateDTO.getObjetRelance().contains("#ANNEE")) {
                throw new BadRequestException("In reminder object pattern is missing: #ANNEE");
            }

            if(emailTemplateDTO.getFormulaireDateLimiteReponse().before(new Date())) {
                throw new BadRequestException("Date limite of response has to be after today date");
            }

                       dbPeRefFormulaire.setMailInitial(emailTemplateDTO.getMailInitial());
                       dbPeRefFormulaire.setMailRelance(emailTemplateDTO.getMailRelance());
                       dbPeRefFormulaire.setObjetInitial(emailTemplateDTO.getObjetInitial());
                       dbPeRefFormulaire.setObjetRelance(emailTemplateDTO.getObjetRelance());
                       dbPeRefFormulaire.setFormulaireDateLimiteReponse(emailTemplateDTO.getFormulaireDateLimiteReponse());
                       return peRefFormulaireRepository.save(dbPeRefFormulaire);

        } catch (NumberFormatException numberFormatException) {
            throw new BadRequestException("Id number bad format");
        }
    }
}
