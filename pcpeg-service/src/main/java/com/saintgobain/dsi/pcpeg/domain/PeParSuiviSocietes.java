package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_PAR_SUIVI_SOCIETES")
public class PeParSuiviSocietes implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "societeSid", column = @Column(name = "SOCIETE_SID", nullable = false, precision = 6, scale = 0)),
            @AttributeOverride(name = "anneeId", column = @Column(name = "ANNEE_ID", nullable = false, precision = 4, scale = 0)),
            @AttributeOverride(name = "formulaireId", column = @Column(name = "FORMULAIRE_ID", nullable = false, precision = 1, scale = 0)) })
    private PeParSuiviSocietesId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUT_ID", nullable = false)
    private PeRefStatutFormulaire peRefStatutFormulaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "SOCIETE_SID", referencedColumnName = "SOCIETE_SID", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "ANNEE_ID", referencedColumnName = "ANNEE_ID", nullable = false, insertable = false, updatable = false) })
    private PeParSociete peParSociete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORMULAIRE_ID", nullable = false, insertable = false, updatable = false)
    private PeRefFormulaire peRefFormulaire;

    @Column(name = "FLAG_CHANGEMENT", nullable = false, precision = 1, scale = 0)
    private Boolean flagChangement;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

}
