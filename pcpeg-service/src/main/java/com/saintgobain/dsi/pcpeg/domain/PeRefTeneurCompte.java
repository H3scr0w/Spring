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
@Table(name = "PE_REF_TENEUR_COMPTE")
public class PeRefTeneurCompte implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "TENEUR_COMPTE_ID", unique = true, nullable = false, precision = 3, scale = 0)
    private Short teneurCompteId;

    @Column(name = "TENEUR_COMPTE_LIBELLE", length = 50)
    private String teneurCompteLibelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peRefTeneurCompte")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeDimFonds> peDimFondses;

}
