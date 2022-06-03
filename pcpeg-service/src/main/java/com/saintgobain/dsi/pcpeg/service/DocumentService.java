package com.saintgobain.dsi.pcpeg.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.saintgobain.dsi.pcpeg.config.Constants;
import com.saintgobain.dsi.pcpeg.config.PcpegProperties;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParAccords;
import com.saintgobain.dsi.pcpeg.domain.PeParVersement;
import com.saintgobain.dsi.pcpeg.domain.PeRefTypeAccord;
import com.saintgobain.dsi.pcpeg.dto.DocumentDTO;
import com.saintgobain.dsi.pcpeg.exception.BadRequestException;
import com.saintgobain.dsi.pcpeg.exception.PcpegException;
import com.saintgobain.dsi.pcpeg.repository.PeParAccordsRepository;
import com.saintgobain.dsi.pcpeg.repository.PeParVersementRepository;
import com.saintgobain.dsi.pcpeg.repository.PeRefTypeAccordRepository;
import com.saintgobain.dsi.pcpeg.security.AccessControl;
import com.saintgobain.dsi.pcpeg.service.utils.StreamUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final PeParAccordsRepository documentRepository;

    private final PeParVersementRepository perPaymentRepository;

    private final PeRefTypeAccordRepository agreementTypeRepository;

    private final PcpegProperties properties;

    private final AccessControl accessControl;

    @Transactional
    public void saveDocuments(PeDimUtilisateurs logUser, MultipartFile[] files, List<DocumentDTO> documents)
            throws IOException, PcpegException {

        if (files == null) {
            return;
        }

        checkDocuments(files, documents);

        documents = documents.stream().distinct().collect(Collectors.toList());
        List<MultipartFile> filesUnique = Stream
                .of(files)
                .filter(StreamUtils.distinctByKey(file -> file.getName()))
                .collect(Collectors.toList());
        files = filesUnique.toArray(new MultipartFile[filesUnique.size()]);

        String folder = properties.getCommonLocation() + File.separator;

        Path path = Paths.get(folder);

        File directory = new File(folder);

        if (!directory.exists()) {
            Files.createDirectories(path);
        }

        if (files != null && files.length >= 0) {
            for (int i = 0; i < files.length; i++) {
                String originalFileName = files[i].getOriginalFilename();
                String tempFileName = originalFileName;
                int lastSlash = originalFileName.lastIndexOf("\\");
                if (lastSlash != -1) {
                    tempFileName = originalFileName.substring(lastSlash + 1);
                }
                final String fileName = tempFileName;
                String filesPath = folder + File.separator + fileName;

                DocumentDTO document = documents.stream().filter(doc -> StringUtils.endsWithIgnoreCase(doc
                        .getDocumentName(), fileName)).findFirst().orElse(null);

                if (document != null) {

                    PeRefTypeAccord agreementType = agreementTypeRepository.findById(document.getDocumentType())
                            .orElseThrow(() -> new EntityNotFoundException("Document type not found : " + document
                                    .getDocumentType()));

                    PeParVersement perPayment = perPaymentRepository.findById(document.getPaymentId()).orElseThrow(
                            () -> new EntityNotFoundException("Payment type not found: " + document.getPaymentId()));

                    PeParAccords agreement = PeParAccords.builder()
                            .dateDebut(document.getStartDate())
                            // If no end date set, we set to 31 December 2099
                            .dateFin(document.getEndDate() != null ? document.getEndDate() : new Date(4102358400000L))
                            .lienPdf(filesPath)
                            .logDateMaj(new Date())
                            .logUtilisateurId(logUser != null ? logUser.getUtilisateurId() : Short
                                    .parseShort("-1"))
                            .nomDocument(fileName)
                            .peRefTypeAccord(agreementType)
                            .peParVersement(perPayment)
                            .build();

                    documentRepository.save(agreement);

                } else {
                    throw new BadRequestException("Filename : " + fileName + " does not match with Document request");
                }
            }
            saveFiles(files);
        }

    }

    @Transactional
    public void deleteDocuments(List<DocumentDTO> documents) throws IOException {
        if (CollectionUtils.isNotEmpty(documents)) {
            List<Integer> docIds = documents.stream().map(doc -> doc.getDocumentId()).collect(Collectors
                    .toList());
            List<PeParAccords> agreements = documentRepository.findAllById(docIds);
            documentRepository.deleteAll(agreements);
            for (DocumentDTO doc : documents) {
                try {
                    Files.deleteIfExists(Paths.get(properties.getCommonLocation(), doc.getDocumentName()));
                } catch (NoSuchFileException e) {
                    log.warn("Document not found : " + doc.getDocumentName());
                }
            }

        }

    }

    @Transactional
    public void deleteAgreements(List<PeParAccords> agreements) throws IOException {
        if (CollectionUtils.isNotEmpty(agreements)) {
            documentRepository.deleteAll(agreements);
            for (PeParAccords doc : agreements) {
                try {
                    Files.deleteIfExists(Paths.get(properties.getCommonLocation(), doc.getNomDocument()));
                } catch (NoSuchFileException e) {
                    log.warn("Document not found : " + doc.getNomDocument());
                }
            }

        }

    }

    @Transactional(readOnly = true)
    public Pair<PeParAccords, byte[]> downloadDocument(Authentication authentication,
            Integer societeSid, Integer documentId) throws IOException {

        accessControl.checkAccessCompanyId(authentication, societeSid);

        PeParAccords document = documentRepository.findById(documentId).orElseThrow(() -> new EntityNotFoundException(
                "Document not found for Id: " + documentId));

        String fileName = StringUtils.endsWithIgnoreCase(document.getNomDocument(), Constants.PDF_FORMAT) ? document
                .getNomDocument()
                : document.getNomDocument() + Constants.PDF_FORMAT;
        Path path = Paths.get(properties.getCommonLocation(), fileName);
        byte[] data = null;
        try {
            data = Files.readAllBytes(path);
        } catch (NoSuchFileException ex) {
            throw new EntityNotFoundException(ex.getMessage());
        }

        return new ImmutablePair<>(document, data);
    }

    private void saveFiles(MultipartFile[] files) throws IllegalStateException, IOException {
        String folder = properties.getCommonLocation() + File.separator;
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getOriginalFilename().replace("C:\\fakepath\\", "");
            String filesPath = folder + File.separator + fileName;
            files[i].transferTo(new File(filesPath));
        }
    }

    private void checkDocuments(MultipartFile[] files, List<DocumentDTO> documents) throws PcpegException {
        for (MultipartFile file : files) {
            if (!StringUtils.endsWithIgnoreCase(file.getOriginalFilename(), Constants.PDF_FORMAT)) {
                throw new BadRequestException("PDF file expected. Received : " + file.getOriginalFilename());
            }
        }

    }

}
