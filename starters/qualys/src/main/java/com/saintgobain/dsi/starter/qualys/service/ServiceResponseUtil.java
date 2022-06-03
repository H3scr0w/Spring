package com.saintgobain.dsi.starter.qualys.service;

import org.apache.commons.lang3.StringUtils;

import com.saintgobain.dsi.starter.qualys.exception.BadRequestException;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.ServiceResponse;

public class ServiceResponseUtil {

    public static void checkResponse(ServiceResponse<?> response) throws QualysException {
        String responseCode = response.getResponseCode();

        if (!StringUtils.equalsIgnoreCase("success", responseCode)) {

            if (response.getResponseErrorDetails() != null) {
                throw new BadRequestException(responseCode + ":" + response.getResponseErrorDetails()
                        .getErrorMessage());
            }

            throw new BadRequestException(responseCode);
        }
    }

}
