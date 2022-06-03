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
@Table(name = "PE_DIM_TYPE_VERSEMENT")
public class PeDimTypeVersement implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // 1 Supplément Participation
    // 2 Participation
    // 3 Supplément Intéressement
    // 4 Intéressement
    // 5 CET

    @Id
    @Column(name = "TYPE_VERSEMENT_SID", unique = true, nullable = false, precision = 2, scale = 0)
    private Integer typeVersementSid;

    @Column(name = "TYPE_VERSEMENT_LIBELLE", nullable = false, length = 50)
    private String typeVersementLibelle;

    @Column(name = "FLAG_EXISTE_INFRA", nullable = false, precision = 1, scale = 0)
    private Boolean flagExisteInfra;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimTypeVersement")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParVersement> peParVersements;

}
