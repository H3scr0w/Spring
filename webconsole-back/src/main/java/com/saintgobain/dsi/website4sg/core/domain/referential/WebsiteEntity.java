package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.saintgobain.dsi.website4sg.core.domain.users.ProjectEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder(toBuilder = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "WEBSITE")
public class WebsiteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEBS_ID")
    private Long websiteId;

    @Column(name = "WEBS_CODE_REPO_URL")
    private String codeRepositoryUrl;

    @Column(name = "WEBS_BIN_REPO_URL")
    private String binaryRepositoryUrl;

    @Column(name = "WEBS_HOME_DIR")
    private String homeDirectory;

    @Column(name = "WEBS_NAME")
    private String name;

    @Column(name = "WEBS_ENABLE")
    private Boolean enable;

    @Column(name = "WEBS_CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "WEBS_LASTUPDATE")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @Column(name = "WEBS_CODE", unique = true)
    private String code;

    @Column(name = "WEBS_QUALYS_WEBAPP_ID", unique = true)
    private Long qualysWebAppId;

    @Column(name = "WEBS_IS_QUALYS_ENABLE")
    private Boolean isQualysEnable;

    @Column(name = "WEBS_IS_MULTI_MAIN_DOMAIN")
    private Boolean isMultiMainDomain;

    @Column(name = "WEBS_IS_TO_BE_DISABLED")
    private Boolean isToBeDisabled;

    @Column(name = "WEBS_ISLIVE")
    @Builder.Default
    private Boolean isLive = false;

    @ToString.Exclude
    @OneToMany(mappedBy = "website", fetch = FetchType.LAZY)
    private List<WebsiteDeployedEntity> websitedeployedByWebsite;

    @ToString.Exclude
    @OneToOne(mappedBy = "website", fetch = FetchType.LAZY)
    private ProjectEntity project;

}