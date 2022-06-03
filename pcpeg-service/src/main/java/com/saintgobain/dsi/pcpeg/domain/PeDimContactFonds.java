package com.saintgobain.dsi.pcpeg.domain;

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
@Table(name = "PE_DIM_CONTACT_FONDS")
public class PeDimContactFonds implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "S_1_PE_DIM_CONTACT_FONDS")
    @GenericGenerator(name = "S_1_PE_DIM_CONTACT_FONDS", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator", parameters = {
            @Parameter(name = "sequence_name", value = "S_1_PE_DIM_CONTACT_FONDS"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "1") })
    @Column(name = "CONTACT_ID", unique = true, nullable = false, precision = 3, scale = 0)
    private Short contactId;

    @Column(name = "NOM_CONTACT", length = 100)
    private String nomContact;

    @Column(name = "EMAIL", length = 100)
    private String email;

    @Column(name = "TELEPHONE", length = 50)
    private String telephone;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "peDimContactFonds")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<PeDimFonds> peDimFondses;

}
