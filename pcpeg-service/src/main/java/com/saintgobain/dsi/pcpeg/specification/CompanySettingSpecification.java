package com.saintgobain.dsi.pcpeg.specification;

import javax.persistence.criteria.Join;

import org.springframework.data.jpa.domain.Specification;

import com.saintgobain.dsi.pcpeg.domain.PeDimTypeVersement;
import com.saintgobain.dsi.pcpeg.domain.PeDimUtilisateurs;
import com.saintgobain.dsi.pcpeg.domain.PeParFondsAutorisesSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParSociete;
import com.saintgobain.dsi.pcpeg.domain.PeParVersement;

public class CompanySettingSpecification {

    public static Specification<PeParFondsAutorisesSociete> yearSpecification(Short year) {
        return (Specification<PeParFondsAutorisesSociete>) (root, query, builder) -> {

            Join<PeParFondsAutorisesSociete, PeParVersement> payment = root.join("peParVersement");
            Join<PeParVersement, PeParSociete> perCompany = payment.join("peParSociete");
            return builder.equal(perCompany.get("id").get(
                    "anneeId"),
                    year);
        };
    }

    public static Specification<PeParFondsAutorisesSociete> companySpecification(Integer societeSid) {
        return (Specification<PeParFondsAutorisesSociete>) (root, query, builder) -> {

            Join<PeParFondsAutorisesSociete, PeParVersement> payment = root.join("peParVersement");
            Join<PeParVersement, PeParSociete> perCompany = payment.join("peParSociete");
            return builder.equal(perCompany.get("id").get(
                    "societeSid"),
                    societeSid);
        };
    }

    public static Specification<PeParFondsAutorisesSociete> companySgidSpecification(String sgid) {
        return (Specification<PeParFondsAutorisesSociete>) (root, query, builder) -> {
            Join<PeParFondsAutorisesSociete, PeParVersement> payment = root.join("peParVersement");
            Join<PeParVersement, PeParSociete> perCompany = payment.join("peParSociete");
            Join<PeParSociete, PeDimUtilisateurs> peDimUtilisateurs = perCompany.join("peDimUtilisateurs");
            return builder.equal(peDimUtilisateurs.get("sgid"), sgid);
        };
    }

    public static Specification<PeParFondsAutorisesSociete> paymentTypeSpecification(Integer paymentTypeId) {
        return (Specification<PeParFondsAutorisesSociete>) (root, query, builder) -> {

            Join<PeParFondsAutorisesSociete, PeParVersement> payment = root.join("peParVersement");
            Join<PeParVersement, PeDimTypeVersement> paymentType = payment.join("peDimTypeVersement");
            return builder.equal(paymentType.get("typeVersementSid"),
                    paymentTypeId);
        };
    }

}
