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
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DRUPALDOCROOTCORE")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrupalDocrootCoreEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DDC_ID")
    private Long drupalDocrootCoreId;

    @Column(name = "DDC_NAME")
    private String name;

    @Column(name = "DDC_CODE_REPO_URL")
    private String codeRepositoryUrl;

    @Column(name = "DDC_BIN_REPO_URL")
    private String binaryRepositoryUrl;

    @Column(name = "DDC_CODE", unique = true)
    private String code;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "drupaldocrootcore", fetch = FetchType.LAZY)
    private List<DocrootEnvironmentEntity> docrootenvironmentByDrupalDocrootCore;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne(mappedBy = "drupaldocrootcore", fetch = FetchType.LAZY)
    private ProjectEntity project;
}