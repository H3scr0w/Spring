package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PE_PAR_HABILITATIONS")
public class PeParHabilitations implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_PAR_HABILITATIONS")
    @GenericGenerator(name = "S_1_PE_PAR_HABILITATIONS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_PAR_HABILITATIONS"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "HABILITATIONS_SID", unique = true, nullable = false, precision = 4, scale = 0)
    private Short habilitationsSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({ @JoinColumn(name = "SOCIETE_SID", referencedColumnName = "SOCIETE_SID"),
            @JoinColumn(name = "ANNEE_ID", referencedColumnName = "ANNEE_ID") })
    @EqualsAndHashCode.Exclude
    private PeParSociete peParSociete;

    @Column(name = "ETABLISSEMENT_ID", length = 5)
    private String etablissementId;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumns({ @JoinColumn(name = "SOCIETE_SID", referencedColumnName = "SOCIETE_SID", insertable = false, updatable = false),
                   @JoinColumn(name = "ETABLISSEMENT_ID", referencedColumnName = "ETABLISSEMENT_SID", insertable = false, updatable = false) })
    @EqualsAndHashCode.Exclude
    private PeDimEtablissement etablissement;

    @Column(name = "SGID", nullable = false, length = 8)
    private String sgid;

    @Column(name = "NOM", length = 50)
    private String nom;

    @Column(name = "PRENOM", length = 50)
    private String prenom;

    @Column(name = "EMAIL", length = 50)
    private String email;

    @Column(name = "TELEPHONE", length = 50)
    private String telephone;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

    @Column(name = "CATEGORY", length = 255)
    private String category;

}
