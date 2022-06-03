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
import javax.persistence.Lob;
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
@Table(name = "CERTIFICATE")
@Convert(converter = CryptoPassword.class, attributeName = "passphrase")
public class CertificateEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CERT_ID")
    private Long certificateId;

    @Column(name = "CERT_CODE", unique = true)
    private String code;

    @Column(name = "CERT_NAME")
    private String name;

    @Lob
    @Column(name = "CERT_VALUE")
    @ToString.Exclude
    private byte[] value;

    @Lob
    @Column(name = "CERT_KEY")
    @ToString.Exclude
    private byte[] key;

    @Column(name = "CERT_PASSPHRASE")
    @ToString.Exclude
    private String passphrase;

    @Lob
    @Column(name = "CERT_INTERMEDIATE")
    @ToString.Exclude
    private byte[] intermediate;

    @Lob
    @Column(name = "CERT_CSR")
    @ToString.Exclude
    private byte[] csr;

    @Column(name = "CERT_CREATED")
    @Temporal(TemporalType.DATE)
    private Date created;

    @Column(name = "CERT_LASTUPDATE")
    @Temporal(TemporalType.DATE)
    private Date lastUpdate;

    @Column(name = "CERT_UPDATEDBY")
    private String updatedBy;

    @ManyToMany(mappedBy = "certificateByDocrootenvironment", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DocrootEnvironmentEntity> docrootenvironmentByCertificate;

}