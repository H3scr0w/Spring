package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ENVIRONMENT")
public class EnvironmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ENVI_ID")
    private Long environmentId;

    @Column(name = "ENVI_CODE", unique = true)
    private String code;

    @Column(name = "ENVI_NAME")
    private String name;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "environment", fetch = FetchType.LAZY)
    private List<DocrootEnvironmentEntity> docrootenvironmentByEnvironment;

}