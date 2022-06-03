package com.saintgobain.dsi.pcpeg.config;

import java.text.SimpleDateFormat;

/**
 * Application constants.
 */
public final class Constants {
    /**
     * The constant SPRING_PROFILE_DEVELOPMENT.
     */
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    /**
     * The constant SPRING_PROFILE_PRODUCTION.
     */
    public static final String SPRING_PROFILE_PRODUCTION = "prod";

    public static final String REST_OPERATION_REPLACE = "replace";

    public static final String REST_UNIQUE_MEMBER_VALUE = "stgosgi=%s,ou=persons,dc=corpldap,dc=atcsg,dc=net";

    public static String DATE_FORMAT_PATTERN = "dd/MM/yyyy";

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_PATTERN);

    public static String FUND_SPECIFIQUE = "SPE";

    public static String FUND_DIVERS = "DIV";

    public static String FUND_PEG = "PCL";

    public static Integer PAYMENT_TYPE_PARTICIPATION_SUPP = 1;

    public static Integer PAYMENT_TYPE_PARTICIPATION = 2;

    public static Integer PAYMENT_TYPE_INTERET_SUPP = 3;

    public static Integer PAYMENT_TYPE_INTERET = 4;

    public static Integer PAYMENT_TYPE_CET = 5;

    public static String PDF_FORMAT = ".pdf";


    private Constants() {}
}
