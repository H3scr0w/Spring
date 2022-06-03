package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
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
@Table(name = "V_CAMPAIGN")
public class CampaignView implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "societeSid", column = @Column(name = "SOCIETE_SID", nullable = false)),
            @AttributeOverride(name = "anneeId", column = @Column(name = "ANNEE_ID", nullable = false)),
            @AttributeOverride(name = "formulaireId", column = @Column(name = "FORMULAIRE_ID", nullable = false)) })
    private PeParSuiviSocietesId id;

    @Column(name = "CODE_SIF")
    private String codeSif;

    @Column(name = "CODE_AMUNDI")
    private String codeAmundi;

    @Column(name = "SOCIETE_LIBELLE")
    private String societeLibelle;

    @Column(name = "FLAG_ADHERENTE")
    private Boolean flagAdherente;

    @Column(name = "FLAG_ACTIF")
    private Boolean active;

    @Column(name = "FLAG_EN_COURS")
    private Boolean flagEnCours;

    @Column(name = "FORMULAIRE_LIBELLE")
    private String formulaireLibelle;

    @Column(name = "STATUT_ID")
    private Short statutId;

    @Column(name = "STATUT_LIBELLE")
    private String statutLibelle;

    @Column(name = "DATE_DERNIER_MAIL")
    private Date dateDernierMail;

    @Column(name = "FLAG_ENVOIE_MAIL")
    private Boolean flagEnvoieMail;

    @Column(name = "FLAG_RELANCE_MAIL")
    private Boolean flagRelanceMail;

    @Column(name = "CORRESPONDANTN_ID")
    private Short correspondantActuelId;

    @Column(name = "CORRESPONDANTN_SGID")
    private String correspondantActuelSgid;

    @Column(name = "CORRESPONDANTN_NOM")
    private String correspondantActuelNom;

    @Column(name = "CORRESPONDANTN_PRENOM")
    private String correspondantActuelPrenom;

    @Column(name = "CORRESPONDANTN_EMAIL")
    private String correspondantActuelEmail;

    @Column(name = "CORRESPONDANTN_TELEPHONE")
    private String correspondantActuelTelephone;

    @Column(name = "CORRESPONDANTN1_ID")
    private Short correspondantPrecedentId;

    @Column(name = "CORRESPONDANTN1_SGID")
    private String correspondantPrecedentSgid;

    @Column(name = "CORRESPONDANTN1_NOM")
    private String correspondantPrecedentNom;

    @Column(name = "CORRESPONDANTN1_PRENOM")
    private String correspondantPrecedentPrenom;

    @Column(name = "CORRESPONDANTN1_EMAIL")
    private String correspondantPrecedentEmail;

    @Column(name = "CORRESPONDANTN1_TELEPHONE")
    private String correspondantPrecedentTelephone;

}
