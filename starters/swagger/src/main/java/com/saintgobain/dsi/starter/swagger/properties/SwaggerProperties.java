package com.saintgobain.dsi.starter.swagger.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Swagger properties for custom config
 */
@ConfigurationProperties(prefix = "saint.gobain.swagger", ignoreUnknownFields = false)
public class SwaggerProperties {

    private String title = "Application API";

    private String description = "API documentation";

    private String version = "1.0";

    private String termsOfServiceUrl;

    private String contactName = "Digital Solutions";

    private String contactUrl = "www.saint-gobain.fr";

    private String contactEmail = "digital-solutions@saint-gobain.ocm";

    private String license = "Owner";

    private String licenseUrl = "www.saint-gobain.fr/";

    private String defaultIncludePattern = "/.*";

    private String authorizationName = "jwt";

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets terms of service url.
     *
     * @return the terms of service url
     */
    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    /**
     * Sets terms of service url.
     *
     * @param termsOfServiceUrl the terms of service url
     */
    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    /**
     * Gets contact name.
     *
     * @return the contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * Sets contact name.
     *
     * @param contactName the contact name
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * Gets contact url.
     *
     * @return the contact url
     */
    public String getContactUrl() {
        return contactUrl;
    }

    /**
     * Sets contact url.
     *
     * @param contactUrl the contact url
     */
    public void setContactUrl(String contactUrl) {
        this.contactUrl = contactUrl;
    }

    /**
     * Gets contact email.
     *
     * @return the contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets contact email.
     *
     * @param contactEmail the contact email
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Gets license.
     *
     * @return the license
     */
    public String getLicense() {
        return license;
    }

    /**
     * Sets license.
     *
     * @param license the license
     */
    public void setLicense(String license) {
        this.license = license;
    }

    /**
     * Gets license url.
     *
     * @return the license url
     */
    public String getLicenseUrl() {
        return licenseUrl;
    }

    /**
     * Sets license url.
     *
     * @param licenseUrl the license url
     */
    public void setLicenseUrl(String licenseUrl) {
        this.licenseUrl = licenseUrl;
    }

    /**
     * Gets default include pattern.
     *
     * @return the default include pattern
     */
    public String getDefaultIncludePattern() {
        return defaultIncludePattern;
    }

    /**
     * Sets default include pattern.
     *
     * @param defaultIncludePattern the default include pattern
     */
    public void setDefaultIncludePattern(String defaultIncludePattern) {
        this.defaultIncludePattern = defaultIncludePattern;
    }

    /**
     * Gets authorization name.
     *
     * @return the authorization name
     */
    public String getAuthorizationName() {
        return authorizationName;
    }

    /**
     * Sets authorization name.
     *
     * @param authorizationName the authorization name
     */
    public void setAuthorizationName(String authorizationName) {
        this.authorizationName = authorizationName;
    }
}
