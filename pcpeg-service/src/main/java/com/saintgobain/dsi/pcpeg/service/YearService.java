package com.saintgobain.dsi.pcpeg.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.saintgobain.dsi.pcpeg.domain.PeDimAnnee;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimAnneeRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class YearService {

    private final PeDimAnneeRepository peDimAnneeRepository;

    @Transactional(readOnly = true)
    public List<PeDimAnnee> getYears() throws PcpegException {
        return peDimAnneeRepository.findAll(Sort.by(Sort.Direction.DESC, "anneeId"));
    }

}
