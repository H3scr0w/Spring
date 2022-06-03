package com.saintgobain.dsi.pcpeg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PeDimAnneeService {
    private final PeDimAnneeRepository peDimAnneeRepository;

    @Transactional
    public PeDimAnnee createPeDimAnnee(short year) throws PcpegException {
        if (peDimAnneeRepository.findByAnneeId(year).isPresent()) {
            throw new BadRequestException("Year already exists");
        }
        PeDimAnnee peDimAnnee = new PeDimAnnee();

        Optional<PeDimAnnee> previousYear = peDimAnneeRepository.findByFlagEnCours(true);
        previousYear.ifPresent(previousPeDimAnee ->{
            previousPeDimAnee.setFlagEnCours(false);
            peDimAnneeRepository.save(previousPeDimAnee);
        });

        peDimAnnee.setAnneeId(year);
        peDimAnnee.setFlagEnCours(true);

        return peDimAnneeRepository.save(peDimAnnee);
    }

    @Transactional(readOnly = true)
    public Short getPreviousYear() {
        List<PeDimAnnee> peDimAnneeList = peDimAnneeRepository.findTop2ByOrderByAnneeIdDesc();
        return peDimAnneeList.stream().map(peDimAnnee -> (peDimAnnee.getAnneeId())).sorted().findFirst().orElse(null);
    }

    @Transactional(readOnly = true)
    public Short getCurrentYear() {
        PeDimAnnee peDimAnnee = peDimAnneeRepository.findByFlagEnCours(true).orElse(null);
        if (peDimAnnee != null) {
            return peDimAnnee.getAnneeId();
        }
        return null;

    }

    @Transactional(readOnly = true)
    public Short getLastYear() {
        PeDimAnnee peDimAnnee = peDimAnneeRepository.findTop1ByOrderByAnneeIdDesc().orElse(null);
        if (peDimAnnee != null) {
            return peDimAnnee.getAnneeId();
        }
        return null;

    }
}
