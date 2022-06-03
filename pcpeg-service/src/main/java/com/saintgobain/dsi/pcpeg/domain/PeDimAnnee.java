package com.saintgobain.dsi.pcpeg.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PE_DIM_ANNEE")
public class PeDimAnnee implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ANNEE_ID", unique = true, nullable = false, precision = 4, scale = 0)
    private Short anneeId;

    @Column(name = "FLAG_EN_COURS", nullable = false, precision = 1, scale = 0)
    private Boolean flagEnCours;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimAnnee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParSuiviUtilisateurs> peParSuiviUtilisateurses;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimAnnee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParSociete> peParSocietes;

}
