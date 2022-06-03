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
@Table(name = "PE_REF_TYPE_ACCORD")
public class PeRefTypeAccord implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    // 1 Accord
    // 2 Avenant

    @Id
    @Column(name = "TYPE_ACCORD_ID", unique = true, nullable = false, precision = 1, scale = 0)
    private Integer typeAccordId;

    @Column(name = "LIBELLE_TYPE_ACCORD", nullable = false, length = 50)
    private String libelleTypeAccord;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peRefTypeAccord")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeParAccords> peParAccordses;

}
