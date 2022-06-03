package com.saintgobain.dsi.website4sg.core.domain.users;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ACCESSRIGHT")
public class AccessRightEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACRI_ID")
    private Long accessRightId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROJ_ID", nullable = false)
    @ToString.Exclude
    private ProjectEntity project;

    @Column(name = "PROJ_ID", insertable = false, updatable = false)
    private Long projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    @ToString.Exclude
    private UsersEntity users;

    @Column(name = "USER_ID", insertable = false, updatable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROLE_ID", nullable = false)
    @ToString.Exclude
    private RolesEntity roles;

    @Column(name = "ROLE_ID", insertable = false, updatable = false)
    private Long roleId;

}