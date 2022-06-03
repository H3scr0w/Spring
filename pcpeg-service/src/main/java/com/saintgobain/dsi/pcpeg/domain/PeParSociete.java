package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_PAR_SOCIETE")
public class PeParSociete implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "societeSid", column = @Column(name = "SOCIETE_SID", nullable = false, precision = 6, scale = 0)),
            @AttributeOverride(name = "anneeId", column = @Column(name = "ANNEE_ID", nullable = false, precision = 4, scale = 0)) })
    private PeParSocieteId id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "UTILISATEUR_ID")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private PeDimUtilisateurs peDimUtilisateurs;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "SOCIETE_SID", nullable = false, insertable = false, updatable = false)
    private PeDimSociete peDimSociete;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JoinColumn(name = "ANNEE_ID", nullable = false, insertable = false, updatable = false)
    private PeDimAnnee peDimAnnee;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

    @Column(name = "UTILISATEUR_ID_TEMPO", precision = 4, scale = 0)
    private Short utilisateurIdTempo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peParSociete")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<PeParSuiviSocietes> peParSuiviSocieteses = new HashSet<PeParSuiviSocietes>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peParSociete")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<PeParVersement> peParVersements = new HashSet<PeParVersement>(0);

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peParSociete")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<PeParHabilitations> peParHabilitationses = new HashSet<PeParHabilitations>(0);

}
