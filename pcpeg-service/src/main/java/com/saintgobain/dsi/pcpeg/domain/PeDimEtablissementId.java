package com.saintgobain.dsi.pcpeg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PeDimEtablissementId implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @Column(name = "ETABLISSEMENT_SID", nullable = false, length = 5)
    private String facilityId;

    @Column(name = "SOCIETE_SID", nullable = false, precision = 6, scale = 0)
    private Integer societeSid;
}
