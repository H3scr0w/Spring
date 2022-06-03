package com.saintgobain.dsi.pcpeg.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "PE_DIM_GRP_FONDS")
public class PeDimGrpFonds implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // SPE
    // DIV
    // PCL
    // PAI
    // PPRE
    @Id
    @Column(name = "GRP_FONDS_ID", unique = true, nullable = false, length = 4)
    private String grpFondsId;

    // SPE
    // DIV
    // PEG
    // PAI
    // PEG
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TYPE_FONDS_ID")
    private PeDimTypeFonds peDimTypeFonds;

    // Fonds Spécifiques / CCB
    // Formule Diversifiée
    // Formule Classique
    // Paiement
    // Formule Premium
    @Column(name = "GRP_FONDS_LIBELLE", nullable = false, length = 50)
    private String grpFondsLibelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimGrpFonds")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeDimFonds> peDimFondses;

}
