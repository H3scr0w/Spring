package com.saintgobain.dsi.pcpeg.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PE_DIM_ETABLISSEMENT")
public class PeDimEtablissement implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "facilityId", column = @Column(name = "ETABLISSEMENT_SID", nullable = false, precision = 5, scale = 0)),
            @AttributeOverride(name = "societeSid", column = @Column(name = "SOCIETE_SID", nullable = false, precision = 6, scale = 0)) })
    private PeDimEtablissementId id;

    @Column(name = "ETABLISSEMENT_LIBELLE", nullable = false, length = 100)
    private String facilityLabel;

    @Column(name = "ETABLISSEMENT_LIBELLE_COURT", length = 10)
    private String facilityShortLabel;

    @Column(name = "FLAG_ACTIF", length = 1)
    @Builder.Default
    private String isActive = "O";

    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "SOCIETE_SID", referencedColumnName = "SOCIETE_SID", insertable = false, updatable = false, foreignKey = @javax.persistence.ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private PeDimSociete societe;

}
