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
@Table(name = "HOSTINGPROVIDER")
public class HostingProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HOPR_ID")
    private Long hostingProviderId;

    @Column(name = "HOPR_CODE", unique = true)
    private String code;

    @Column(name = "HOPR_NAME")
    private String name;

    @OneToMany(mappedBy = "hostingprovider", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DocrootEntity> docroot;

    @OneToMany(mappedBy = "hostingprovider", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<LoadBalancerEntity> loadbalancerByHostingProvider;

}