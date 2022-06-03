package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "DOMAINTYPE")
public class DomainTypeEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "DOTY_ID", nullable = false, unique = true)
    private String domainTypeId;

    @OneToMany(mappedBy = "domainType", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DomainEntity> domains;

}
