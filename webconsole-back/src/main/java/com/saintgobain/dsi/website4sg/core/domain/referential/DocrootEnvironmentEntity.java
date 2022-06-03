package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "DOCROOTENVIRONMENT")
@Convert(converter = CryptoPassword.class, attributeName = "password")
public class DocrootEnvironmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOEN_ID")
    private Long docrootEnvironmentId;

    @Column(name = "DOEN_CMSVERSION")
    private String cmsVersion;

    @Column(name = "DOEN_DRUPALDOCROOTCOREVERSION")
    private String drupalDocrootCoreVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOCR_ID", nullable = false)
    @ToString.Exclude
    private DocrootEntity docroot;

    @Column(name = "DOCR_ID", insertable = false, updatable = false)
    private Long docrootId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CMS_ID", nullable = false)
    private CmsEntity cms;

    @Column(name = "CMS_ID", insertable = false, updatable = false)
    private Long cmsId;

    @Column(name = "DOEN_REALM")
    private String realm;

    @Column(name = "DOEN_USER")
    private String user;

    @Column(name = "DOEN_PASSWORD")
    private String password;

    @Column(name = "DOEN_IS_BASIC_AUTH")
    private Boolean isBasicAuth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENVI_ID", nullable = false)
    @ToString.Exclude
    private EnvironmentEntity environment;

    @Column(name = "ENVI_ID", insertable = false, updatable = false)
    private Long environmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DDC_ID", nullable = false)
    @ToString.Exclude
    private DrupalDocrootCoreEntity drupaldocrootcore;

    @Column(name = "DDC_ID", insertable = false, updatable = false)
    private Long drupalDocrootCoreId;

    @Column(name = "DOEN_CAN_AUTO_DEPLOY")
    @Builder.Default
    private Boolean canAutoDeploy = false;

    @Column(name = "DOEN_IS_QUALYS_ENABLE")
    private Boolean isQualysEnable;

    @Column(name = "DOEN_QUALYS_WEBAPP_ID", unique = true)
    private Long qualysWebAppId;

    @Column(name = "DOEN_QUALYS_WEBAUTH_ID")
    private String qualysWebAuthId;

    @Column(name = "DOEN_PROVIDER_INTERNAL_ID", unique = true)
    private String providerInternalId;

    @Column(name = "DOEN_ACQUIA_ENVIRONMENT_ID", unique = true)
    private String acquiaEnvironmentId;

    @OneToMany(mappedBy = "docrootenvironmentByWebsiteDeployed", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<WebsiteDeployedEntity> websitedeployedByDocrootEnvironment = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOCROOTENVSERVERS", joinColumns = @JoinColumn(name = "FK_DOEN_ID", referencedColumnName = "DOEN_ID"), inverseJoinColumns = @JoinColumn(name = "FK_SERV_ID", referencedColumnName = "SERV_ID"))
    @Builder.Default
    @ToString.Exclude
    private List<ServerEntity> server = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOCROOTENVDOMAIN", joinColumns = @JoinColumn(name = "FK_DOEN_ID", referencedColumnName = "DOEN_ID"), inverseJoinColumns = @JoinColumn(name = "FK_DOMA_ID", referencedColumnName = "DOMA_ID"))
    @Builder.Default
    @ToString.Exclude
    private List<DomainEntity> domain = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOCROOTENVLOADBLANCER", joinColumns = @JoinColumn(name = "FK_DOEN_ID", referencedColumnName = "DOEN_ID"), inverseJoinColumns = @JoinColumn(name = "FK_LOBA_ID", referencedColumnName = "LOBA_ID"))
    @Builder.Default
    @ToString.Exclude
    private List<LoadBalancerEntity> loadbalancerByDocrootEnvironment = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "DOCROOTENVCERTIF", joinColumns = @JoinColumn(name = "FK_DOEN_ID", referencedColumnName = "DOEN_ID"), inverseJoinColumns = @JoinColumn(name = "FK_CERT_ID", referencedColumnName = "CERT_ID"))
    @Builder.Default
    @ToString.Exclude
    private List<CertificateEntity> certificateByDocrootenvironment = new ArrayList<>();

}