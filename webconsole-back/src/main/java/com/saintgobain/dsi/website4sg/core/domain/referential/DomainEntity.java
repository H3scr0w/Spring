package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
@Table(name = "DOMAIN")
@Convert(converter = CryptoPassword.class, attributeName = "password")
public class DomainEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOMA_ID", unique = true)
    private Long domainId;

    @Column(name = "DOMA_CODE", unique = true)
    private String code;

    @Column(name = "DOMA_NAME")
    private String name;

    @Column(name = "DOMA_HTTPSENABLE")
    private Boolean httpsEnable;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGI_ID", nullable = true)
    @ToString.Exclude
    private RegistarEntity registar;

    @Column(name = "REGI_ID", insertable = false, updatable = false)
    private Long registarId;

    @Column(name = "DOMA_WAFID")
    private String wafId;

    @Column(name = "DOMA_REALM")
    private String realm;

    @Column(name = "DOMA_USER")
    private String user;

    @Column(name = "DOMA_PASSWORD")
    @ToString.Exclude
    private String password;

    @Column(name = "DOMA_USE_DOEN_AUTH")
    private Boolean useDocrootEnvAuth;

    @Column(name = "DOMA_IS_BASIC_AUTH")
    private Boolean isBasicAuth;

    @Column(name = "DOMA_IS_QUALYS_ENABLE")
    private Boolean isQualysEnable;

    @Column(name = "DOMA_QUALYS_WEBAPP_ID", unique = true)
    private Long qualysWebAppId;

    @Column(name = "DOMA_QUALYS_WEBAUTH_ID")
    private String qualysWebAuthId;

    @Column(name = "DOMA_IS_MONITOR_ENABLE")
    private Boolean isMonitorEnable;

    @Column(name = "DOMA_MONITOR_KEYWORD")
    private String monitorKeyword;

    @ManyToMany(mappedBy = "domain", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DocrootEnvironmentEntity> docrootenvironment;

    @Column(name = "DOTY_ID", nullable = false, insertable = false, updatable = false)
    private String domainTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOTY_ID", nullable = false)
    @ToString.Exclude
    private DomainTypeEntity domainType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "DOMA_ID")
    @ToString.Exclude
    private DomainEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DomainEntity> children;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "WEDE_ID", nullable = false)
    @ToString.Exclude
    private WebsiteDeployedEntity websiteDeployed;

}