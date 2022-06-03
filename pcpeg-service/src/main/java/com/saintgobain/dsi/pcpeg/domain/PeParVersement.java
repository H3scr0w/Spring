package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "PE_PAR_VERSEMENT")
public class PeParVersement implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_PAR_VERSEMENT")
    @GenericGenerator(name = "S_1_PE_PAR_VERSEMENT", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_PAR_VERSEMENT"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "PAR_VERSEMENT_SID", unique = true, nullable = false, precision = 8, scale = 0)
    private Integer parVersementSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "SOCIETE_SID", referencedColumnName = "SOCIETE_SID"),
            @JoinColumn(name = "ANNEE_ID", referencedColumnName = "ANNEE_ID") })
    @EqualsAndHashCode.Exclude
    private PeParSociete peParSociete;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_VERSEMENT_SID", nullable = false)
    @EqualsAndHashCode.Exclude
    private PeDimTypeVersement peDimTypeVersement;

    @Column(name = "FLAG_BLOC_PLIE", nullable = false, precision = 1, scale = 0)
    private Boolean flagBlocPlie;

    @Column(name = "FLAG_VERSEMENT", nullable = false, precision = 1, scale = 0)
    private Boolean flagVersement;

    @Column(name = "FLAG_VERSEMENT_INFRA", nullable = false, precision = 1, scale = 0)
    private Boolean flagVersementInfra;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peParVersement")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParFondsAutorisesSociete> peParFondsAutorisesSocietes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peParVersement")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParAccords> peParAccordses;

}
