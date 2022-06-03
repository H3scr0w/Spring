package com.saintgobain.dsi.pcpeg.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PeParFondsAutorisesSocieteId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "FONDS_SID", nullable = false, precision = 3, scale = 0)
    private Short fondsSid;

    @Column(name = "PAR_VERSEMENT_SID", nullable = false, precision = 8, scale = 0)
    private Integer parVersementSid;

}
