package com.saintgobain.dsi.website4sg.core.domain.scan;

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
@Table(name = "SCANSTATUS")
public class ScanStatusEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "SCST_ID")
    private String scanStatusId;

    @OneToMany(mappedBy = "scanStatus", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ScanEntity> scanByScanStatus;

}
