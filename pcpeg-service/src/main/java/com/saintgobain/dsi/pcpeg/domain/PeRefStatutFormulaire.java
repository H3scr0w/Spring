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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_REF_STATUT_FORMULAIRE")
public class PeRefStatutFormulaire implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // 1
    // 2
    // 3
    // 4

    @Id
    @Column(name = "STATUT_ID", unique = true, nullable = false, precision = 1, scale = 0)
    private Short statutId;

    //
    // Non commencé
    // En cours
    // Validé Correspondant
    // Validé

    @Column(name = "STATUT_LIBELLE", nullable = false, length = 50)
    private String statutLibelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peRefStatutFormulaire")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParSuiviSocietes> peParSuiviSocieteses;

}
