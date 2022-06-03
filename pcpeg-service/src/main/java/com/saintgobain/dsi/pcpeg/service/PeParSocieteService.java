package com.saintgobain.dsi.pcpeg.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saintgobain.dsi.pcpeg.domain.PeDimSociete;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSocieteId;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeDimUtilisateursRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParSocieteRepository;
import com.saintgobain.dsi.pcpeg.service.utils.Partition;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PeParSocieteService {
    private final PeParSocieteRepository peParSocieteRepository;
    private final PeDimAnneeService peDimAnneeService;
    private final PeDimUtilisateursRepository usersRepository;

    @Transactional
    public List<PeParSociete> createPeParSociete(Short year, List<PeDimSociete> activeCompanies, Boolean copyPrevious)
            throws PcpegException {
        List<PeParSociete> savedPeParSociete = new ArrayList<>();
        Short previousYear = peDimAnneeService.getPreviousYear();
        List<Integer> societeIds =  activeCompanies.stream().map(PeDimSociete::getSocieteSid).collect(Collectors.toList());
        Collection<List<Integer>> societeIdsPartitions = Partition.ofSize(
                societeIds.stream().distinct().collect(Collectors.toList()),
                900);
        List<PeParSociete> peParSocieteList = new ArrayList<>();
        // fetch respecting oracle in size
        for (List<Integer> societeIdsPartition : societeIdsPartitions) {
            peParSocieteList.addAll(peParSocieteRepository.findAllById_AnneeIdAndId_SocieteSidIn(year,societeIdsPartition));
        }

        PeDimUtilisateurs adminUser = usersRepository.findFirstBySgid(SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal().toString()).orElse(null);
        Optional<PeParSociete> previousPeParSociete= null;
        for (PeDimSociete activeCompany : activeCompanies) {
            if(BooleanUtils.isTrue(copyPrevious) && previousYear != null){
                previousPeParSociete = peParSocieteRepository.findFirstById_SocieteSidAndId_AnneeId(activeCompany.getSocieteSid(), previousYear);
            }
            if (peParSocieteList.stream().noneMatch(perCompany -> perCompany.getPeDimSociete()
                    .getSocieteSid() == activeCompany.getSocieteSid())) {
                PeParSocieteId peParSocieteId = new PeParSocieteId();
                peParSocieteId.setAnneeId(year);
                peParSocieteId.setSocieteSid(activeCompany.getSocieteSid());
                PeParSociete peParSociete = peParSocieteRepository.findById(peParSocieteId).orElse(new PeParSociete());
                peParSociete.setId(peParSocieteId);
                peParSociete.setPeDimUtilisateurs(previousPeParSociete != null && previousPeParSociete.isPresent() ? previousPeParSociete.get().getPeDimUtilisateurs() : null);
                peParSociete.setLogUtilisateurId(adminUser !=null ? adminUser.getUtilisateurId():-1);
                peParSociete.setLogDateMaj(new Date());
                peParSocieteRepository.save(peParSociete);
                savedPeParSociete.add(peParSociete);

            }
        }

        return savedPeParSociete;
    }

}
