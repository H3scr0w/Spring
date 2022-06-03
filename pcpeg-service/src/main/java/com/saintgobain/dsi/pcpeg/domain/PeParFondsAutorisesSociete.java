package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_PAR_FONDS_AUTORISES_SOCIETE")
public class PeParFondsAutorisesSociete implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "fondsSid", column = @Column(name = "FONDS_SID", nullable = false, precision = 3, scale = 0)),
            @AttributeOverride(name = "parVersementSid", column = @Column(name = "PAR_VERSEMENT_SID", nullable = false, precision = 8, scale = 0)) })
    private PeParFondsAutorisesSocieteId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "PAR_VERSEMENT_SID", nullable = false, insertable = false, updatable = false)
    private PeParVersement peParVersement;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "FONDS_SID", nullable = false, insertable = false, updatable = false)
    private PeDimFonds peDimFonds;

    @Column(name = "FLAG_ACTIF", nullable = false, precision = 1, scale = 0)
    private Boolean flagActif;

    @Column(name = "FLAG_FONDS_DEFAUT", nullable = false, precision = 1, scale = 0)
    private Boolean flagFondsDefaut;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

}
