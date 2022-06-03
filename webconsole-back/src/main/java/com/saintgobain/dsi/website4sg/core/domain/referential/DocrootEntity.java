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
@Table(name = "DOCROOT")
public class DocrootEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DOCR_ID")
    private Long docrootId;

    @Column(name = "DOCR_CODE", unique = true)
    private String code;

    @Column(name = "DOCR_NAME")
    private String name;

    @Column(name = "DOCR_RUNDECK_JOB_API_URL")
    private String rundeckJobApiUrl;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "docroot", fetch = FetchType.LAZY)
    private List<DocrootEnvironmentEntity> docrootenvironmentByDocroot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOPR_ID", nullable = false)
    @ToString.Exclude
    private HostingProviderEntity hostingprovider;

    @Column(name = "HOPR_ID", insertable = false, updatable = false)
    private Long hostingProviderId;

    @Column(name = "DOCR_IS_MUTUALIZED")
    @Builder.Default
    private Boolean isMutualized = false;

    @Column(name = "DOCR_PROVIDER_INTERNAL_ID", unique = true)
    private String providerInternalId;
}