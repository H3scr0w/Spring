package com.saintgobain.dsi.pcpeg.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_DIM_CSP")
public class PeDimCsp implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CSP_ID", unique = true, nullable = false, length = 3)
    private String cspId;

    @Column(name = "CSP_LIBELLE", nullable = false, length = 50)
    private String cspLabel;

    @Column(name = "ACTIF_FL", precision = 1, scale = 0)
    private Boolean isActive;

    @Column(name = "COMMENTAIRE", columnDefinition = "TEXT")
    private String comments;

}
