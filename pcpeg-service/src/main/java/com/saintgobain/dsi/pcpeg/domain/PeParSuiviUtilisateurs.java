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
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_PAR_SUIVI_UTILISATEURS")
public class PeParSuiviUtilisateurs implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "utilisateurId", column = @Column(name = "UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)),
            @AttributeOverride(name = "anneeId", column = @Column(name = "ANNEE_ID", nullable = false, precision = 4, scale = 0)),
            @AttributeOverride(name = "formulaireId", column = @Column(name = "FORMULAIRE_ID", nullable = false, precision = 1, scale = 0)) })
    private PeParSuiviUtilisateursId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FORMULAIRE_ID", nullable = false, insertable = false, updatable = false)
    private PeRefFormulaire peRefFormulaire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UTILISATEUR_ID", nullable = false, insertable = false, updatable = false)
    private PeDimUtilisateurs peDimUtilisateurs;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ANNEE_ID", nullable = false, insertable = false, updatable = false)
    private PeDimAnnee peDimAnnee;

    @Column(name = "FLAG_RELANCE_MAIL", nullable = false, precision = 1, scale = 0)
    private Boolean flagRelanceMail;

    @Column(name = "FLAG_ENVOIE_MAIL", nullable = false, precision = 1, scale = 0)
    private Boolean flagEnvoieMail;

    @Column(name = "FLAG_ACTIF", nullable = false, precision = 1, scale = 0)
    private Boolean flagActif;

    @Column(name = "DATE_DERNIER_MAIL")
    private Date dateDernierMail;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

}
