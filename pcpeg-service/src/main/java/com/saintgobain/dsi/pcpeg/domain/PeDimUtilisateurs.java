package com.saintgobain.dsi.pcpeg.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PE_DIM_UTILISATEURS")
public class PeDimUtilisateurs implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_DIM_UTILISATEURS")
    @GenericGenerator(name = "S_1_PE_DIM_UTILISATEURS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_DIM_UTILISATEURS"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "UTILISATEUR_ID", unique = true, nullable = false, precision = 4, scale = 0)
    private Short utilisateurId;

    @Column(name = "SGID", nullable = false, length = 8)
    private String sgid;

    @Column(name = "NOM", nullable = false, length = 50)
    private String nom;

    @Column(name = "PRENOM", length = 50)
    private String prenom;

    @Column(name = "EMAIL", nullable = false, length = 50)
    private String email;

    @Column(name = "TELEPHONE", length = 50)
    private String telephone;

    @Column(name = "FLAG_ACTIF", nullable = false, precision = 1, scale = 0)
    private Boolean flagActif;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimUtilisateurs")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParSociete> peParSocietes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimUtilisateurs")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParSuiviUtilisateurs> peParSuiviUtilisateurses;

}
