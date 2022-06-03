package com.saintgobain.dsi.pcpeg.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.domain.PeDimCsp;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimCspRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CspService {

    private final PeDimCspRepository peDimCspRepository;

    @Transactional(readOnly = true)
    public List<PeDimCsp> getAllCsp() throws PcpegException {
        return peDimCspRepository.findAll();
    }

}