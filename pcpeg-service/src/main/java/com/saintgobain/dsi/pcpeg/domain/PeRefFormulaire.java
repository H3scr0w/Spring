package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "PE_REF_FORMULAIRE")
public class PeRefFormulaire implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "FORMULAIRE_ID", unique = true, nullable = false, precision = 1, scale = 0)
    private Short formulaireId;

    @Column(name = "FORMULAIRE_LIBELLE", length = 50)
    private String formulaireLibelle;

    @Column(name = "DATE_LIMITE_REPONSE", nullable = true)
    private Date formulaireDateLimiteReponse;

    @Column(name = "OBJET_INITIAL", nullable = true)
    private String objetInitial;

    @Column(name = "OBJET_RELANCE", nullable = true)
    private String objetRelance;

    @Lob
    @Column(name = "EMAIL_INITIAL", nullable = true)
    private String mailInitial;

    @Lob
    @Column(name = "EMAIL_RELANCE", nullable = true)
    private String mailRelance;
}
