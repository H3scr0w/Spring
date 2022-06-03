package com.saintgobain.dsi.pcpeg.service.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

public enum ExcelFileHeaders {

    // Excel Headers

    codeSif("Code SIF"),

    companyLabel("Société Libellé"),

    codeAmundi("Code Amundi"),

    societeLibelle("Société Libellé"),

    flagAdherente("Flag Adhérent"),

    cspId("Csp Paie"),

    comments("Commentaires"),

    correspondantN("Correspondant N"),

    correspondantN1("Correspondant N1"),

    correspondantActuelEmail("Email Correspondant N"),
    
    correspondantPrecedentEmail("Email Correspondant N1"),

    amundiCode("Code Amundi"),

    fundLabel("Nom du fonds"),

    teneurCompteLibelle("Teneur de compte"),

    fundGroupId("Groupe du fonds"),

    statutLibelle("Statut"),

    dateDernierMail("Date envoi"),

    flagEnvoieMail("Initial email envoyé"),

    flagRelanceMail("Rappel email envoyé"),

    facilityId("Etablissement Id"),

    facilityLabel("Nom de l'établissement"),

    isActive("Actif");

    private String value;

    private ExcelFileHeaders() {}

    private ExcelFileHeaders(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ExcelFileHeaders getIfPresent(String str) {
        return Stream.of(ExcelFileHeaders.values())
            .filter(excelHeader -> StringUtils.equalsIgnoreCase(excelHeader.name(), str))
            .findFirst()
            .get();
    }

    public static List<String> getIfPresent(List<String> strList) {
        return Stream.of(ExcelFileHeaders.values())
            .filter(excelHeader -> strList.contains(excelHeader.name()))
            .map(excelHeader -> excelHeader.name())
            .collect(Collectors.toList());
    }

}
