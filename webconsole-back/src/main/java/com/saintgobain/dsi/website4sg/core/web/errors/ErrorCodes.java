package com.saintgobain.dsi.website4sg.core.web.errors;

public enum ErrorCodes {

    /* ACQUIA */
    ACQUIA_ENVIRONMENT_NOT_FOUND,

    /* ACCESS RIGHT */
    ACCESS_RIGHT_ALREADY_EXISTS,

    ACCESS_RIGHT_NOT_FOUND,

    /* COMMON */
    BAD_FETCH_LIMIT,

    /* CMS */
    CMS_NOT_FOUND,

    /* CERTIFICATE */
    CERTIFICATE_NOT_FOUND,

    CERTIFICATE_FILE_BAD_FORMAT,

    CERTIFICATE_KEY_BAD_FORMAT,

    /* DEPLOYMENT */
    DEPLOYMENT_NOT_FOUND,

    DEPLOYMENT_STATUS_NOT_FOUND,

    DEPLOYMENT_TYPE_NOT_FOUND,

    /* DOCROOT */
    DOCROOT_NOT_FOUND,

    /* DOCROOTENVIRONMENT */
    DOCROOTENVIRONMENT_NOT_FOUND,

    /* DOMAIN */
    DOMAIN_NOT_FOUND,

    DOMAIN_QUALYS_NOT_ENABLED,

    DOMAIN_WAFID_NOT_FOUND,

    /* DOMAIN LOADBALANCER */
    DOMAIN_LOADBALANCER_NOT_FOUND,

    /* DOMAIN TYPE */
    DOMAIN_TYPE_NOT_FOUND,

    /* DOMAIN MAIN PARENT */
    DOMAIN_MAIN_CANNOT_HAVE_PARENT,

    /* DOMAIN CONTRIBUTION PARENT */
    DOMAIN_CONTRIBUTION_CANNOT_HAVE_PARENT,

    /* DOMAIN CONTRIBUTION CHILD */
    DOMAIN_CONTRIBUTION_CANNOT_HAVE_CHILD,

    /* DOMAIN REDIRECTION CHILD */
    DOMAIN_REDIRECTION_CANNOT_HAVE_CHILD,

    /* DOMAIN TRANSFER OTHER SITE FORBIDDEN */
    DOMAIN_TRANSFER_OTHER_SITE_FORBIDDEN,

    /* DOMAIN TRANSFER CONTRIBUTION OR MAIN ONLY */
    DOMAIN_TRANSFER_CONTRIBUTION_OR_MAIN_ONLY,

    /* DRUPAL */
    DRUPAL_PROJECT_NOT_FOUND,

    /* ENVIRONMENT */
    ENVIRONMENT_NOT_FOUND,

    /* HOSTING PROVIDER */
    HOSTING_PROVIDER_NOT_FOUND,

    HOSTING_PROVIDER_NOT_ACQUIA,

    /* LOADBALANCER */
    LOADBALANCER_NOT_FOUND,

    /* PROJECT */
    PROJECT_NOT_FOUND,

    /* REGISTAR */
    REGISTAR_NOT_FOUND,

    /* REPORT */
    REPORT_BAD_CONFIG,

    /* ROLE */
    ROLE_NOT_FOUND,

    /* RUNDECK */
    RUNDECK_DEPLOY_KO,

    /* SCAN */
    SCAN_NOT_FOUND,

    SCAN_STATUS_NOT_FOUND,

    SCAN_TYPE_NOT_FOUND,

    /* SERVER */
    SERVER_NOT_FOUND,

    /* STATS */
    STATS_NOT_FOUND,

    /* USER */
    USER_NOT_FOUND,

    /* WEBSITE */
    WEBSITE_PROJECT_NOT_FOUND,

    WEBSITE_NOT_DEPLOYED,

    WEBSITE_DOMAIN_NOT_SAME,

    WEBSITE_QUALYS_NOT_ENABLED
}
