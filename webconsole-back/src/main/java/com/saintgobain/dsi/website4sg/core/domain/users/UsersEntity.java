package com.saintgobain.dsi.website4sg.core.domain.users;

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
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USERS")
public class UsersEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USER_EMAIL", unique = true)
    private String email;

    @Column(name = "USER_FIRSTNAME")
    private String firstname;

    @Column(name = "USER_LASTNAME")
    private String lastname;

    @Column(name = "USER_COMPANY")
    private String company;

    @Column(name = "USER_ISADMIN")
    private Boolean isAdmin;

    @Column(name = "USER_ISACTIVE")
    private Boolean isActive;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<AccessRightEntity> accessrightByUsers;

}