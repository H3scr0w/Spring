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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
@Table(name = "LOADBALANCER")
public class LoadBalancerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOBA_ID")
    private Long loadBalancerId;

    @Column(name = "LOBA_CODE", unique = true)
    private String code;

    @Column(name = "LOBA_IP")
    private String ip;

    @Column(name = "LOBA_IP2")
    private String ip2;

    @Column(name = "LOBA_FQDN")
    private String fqdn;

    @Column(name = "LOBA_NAME")
    private String name;

    @ManyToMany(mappedBy = "loadbalancerByDocrootEnvironment", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DocrootEnvironmentEntity> docrootenvironment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "HOPR_ID", nullable = false)
    @ToString.Exclude
    private HostingProviderEntity hostingprovider;

    @Column(name = "HOPR_ID", insertable = false, updatable = false)
    private Long hostingProviderId;


}