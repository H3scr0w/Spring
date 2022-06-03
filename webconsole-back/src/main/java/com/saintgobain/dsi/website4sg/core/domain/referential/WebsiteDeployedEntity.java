package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "WEBSITESDEPLOYED")
public class WebsiteDeployedEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WEDE_ID")
    private Long websiteDeployedId;

    @Column(name = "WEDE_WEBSITE_VERSION")
    private String websiteVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DOEN_ID", nullable = false)
    @ToString.Exclude
    private DocrootEnvironmentEntity docrootenvironmentByWebsiteDeployed;

    @Column(name = "DOEN_ID", insertable = false, updatable = false)
    private Long docrootEnvironmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WEBS_ID", nullable = false)
    @ToString.Exclude
    private WebsiteEntity website;

    @Column(name = "WEBS_ID", insertable = false, updatable = false)
    private Long websiteId;

    @OneToMany(mappedBy = "websiteDeployed", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DomainEntity> domains;
}