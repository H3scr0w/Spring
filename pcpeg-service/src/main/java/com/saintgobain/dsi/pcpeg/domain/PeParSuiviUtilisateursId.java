package com.saintgobain.dsi.pcpeg.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class PeParSuiviUtilisateursId implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "UTILISATEUR_ID", nullable = false, precision = 4, scale = 0)
    private Short utilisateurId;

    @Column(name = "ANNEE_ID", nullable = false, precision = 4, scale = 0)
    private Short anneeId;

    @Column(name = "FORMULAIRE_ID", nullable = false, precision = 1, scale = 0)
    private Short formulaireId;
}
