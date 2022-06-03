package com.saintgobain.dsi.pcpeg.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
@Table(name = "PE_SYS_REF_CODE_MESSAGE")
public class PeSysRefCodeMessage implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "CODE", unique = true, nullable = false, precision = 3, scale = 0)
    private Short code;

    @Column(name = "LIBELLE", length = 50)
    private String libelle;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peSysRefCodeMessage")
    private Set<PeSysLogMessages> peSysLogMessageses;

}
