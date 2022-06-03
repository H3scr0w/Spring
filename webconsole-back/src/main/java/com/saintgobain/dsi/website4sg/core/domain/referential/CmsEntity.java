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
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CMS")
public class CmsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CMS_ID")
    private Long cmsId;

    @Column(name = "CMS_NAME")
    private String name;

    @Column(name = "CMS_BIN_REPO_URL")
    private String binaryRepositoryUrl;

    @Column(name = "CMS_CODE_REPO_URL")
    private String codeRepositoryUrl;

    @Column(name = "CMS_CODE", unique = true)
    private String code;

    @Column(name = "CMS_ENABLE")
    private Boolean enable;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "cms", fetch = FetchType.LAZY)
    private List<DocrootEnvironmentEntity> docrootenvironmentByCms;

}