package com.saintgobain.dsi.website4sg.core.domain.referential;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@Table(name = "SERVER")
@Convert(converter = CryptoPassword.class, attributeName = "login")
public class ServerEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SERV_ID")
    private Long serverId;

    @Column(name = "SERV_HOSTNAME", unique = true)
    private String hostname;

    @Column(name = "SERV_DOMAIN")
    private String domain;

    @Column(name = "SERV_LOGIN")
    @ToString.Exclude
    private String login;

    @Column(name = "SERV_SSHSERVER")
    private Boolean sshServer;

    @Column(name = "SERV_ENABLE")
    private Boolean enable;

    @Column(name = "SERV_CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "SERV_LASTUPDATE")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @ManyToMany(mappedBy = "server", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DocrootEnvironmentEntity> docrootenvironmentByServer;

}