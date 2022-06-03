package com.saintgobain.dsi.pcpeg.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "PE_DIM_FONDS")
public class PeDimFonds implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_DIM_FONDS")
    @GenericGenerator(name = "S_1_PE_DIM_FONDS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_DIM_FONDS"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "FONDS_SID", unique = true, nullable = false, precision = 3, scale = 0)
    private Short fondsSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "CONTACT_ID")
    private PeDimContactFonds peDimContactFonds;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "TENEUR_COMPTE_ID")
    private PeRefTeneurCompte peRefTeneurCompte;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    @JoinColumn(name = "GRP_FONDS_ID", nullable = false)
    private PeDimGrpFonds peDimGrpFonds;

    @Column(name = "FONDS_ID", nullable = false, length = 10)
    private String fondsId;

    @Column(name = "CODE_FONDS_AMUNDI", length = 10)
    private String codeFondsAmundi;

    @Column(name = "FONDS_LIBELLE", nullable = false)
    private String fondsLibelle;

    @Column(name = "FLAG_EN_COURS_CREATION", nullable = false, precision = 1, scale = 0)
    private Boolean flagEnCoursCreation;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimFonds")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParFondsAutorisesSociete> peParFondsAutorisesSocietes;

}
