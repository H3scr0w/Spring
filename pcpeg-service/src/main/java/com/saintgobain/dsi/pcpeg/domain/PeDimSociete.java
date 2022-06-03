package com.saintgobain.dsi.pcpeg.domain;

import java.util.HashSet;
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
@Table(name = "PE_DIM_SOCIETE")
public class PeDimSociete implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_DIM_SOCIETE")
    @GenericGenerator(name = "S_1_PE_DIM_SOCIETE", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_DIM_SOCIETE"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "SOCIETE_SID", unique = true, nullable = false, precision = 6, scale = 0)
    private Integer societeSid;

    @Column(name = "SOCIETE_LIBELLE", nullable = false, length = 100)
    private String societeLibelle;

    @Column(name = "CODE_SIF", nullable = false, length = 5)
    private String codeSif;

    @Column(name = "CODE_AMUNDI", nullable = false, length = 6)
    private String codeAmundi;

    @Column(name = "FLAG_ADHERENTE", precision = 1, scale = 0)
    private Boolean flagAdherente;

    @Column(name = "FLAG_ACTIF", precision = 1, scale = 0)
    private Boolean active;

    @Column(name = "COMMENTAIRE", columnDefinition = "TEXT")
    private String comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimSociete")
    @EqualsAndHashCode.Exclude
    @Builder.Default
    @ToStringExclude
    private Set<PeParSociete> peParSocietes = new HashSet<PeParSociete>(0);

    @Column(name = "CSP_ID", length = 3)
    private String cspId;

}
