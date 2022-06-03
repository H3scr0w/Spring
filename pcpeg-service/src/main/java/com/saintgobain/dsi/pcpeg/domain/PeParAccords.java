package com.saintgobain.dsi.pcpeg.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.ToStringExclude;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_PAR_ACCORDS")
public class PeParAccords implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_PAR_ACCORDS")
    @GenericGenerator(name = "S_1_PE_PAR_ACCORDS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_PAR_ACCORDS"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "ACCORD_SID", unique = true, nullable = false, precision = 8, scale = 0)
    private Integer accordSid;

    @ManyToOne(fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToStringExclude
    @JoinColumn(name = "PAR_VERSEMENT_SID")
    private PeParVersement peParVersement;

    @OneToOne(fetch = FetchType.EAGER)
    @ToStringExclude
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "TYPE_ACCORD_ID")
    private PeRefTypeAccord peRefTypeAccord;

    @Column(name = "NOM_DOCUMENT", nullable = false, length = 512)
    private String nomDocument;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_DEBUT", nullable = false, length = 7)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATE_FIN", nullable = true, length = 7)
    private Date dateFin;

    @Column(name = "LIEN_PDF", length = 512)
    private String lienPdf;

    @Column(name = "LOG_UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short logUtilisateurId;

    @Column(name = "LOG_DATE_MAJ", nullable = false)
    private Date logDateMaj;

}
