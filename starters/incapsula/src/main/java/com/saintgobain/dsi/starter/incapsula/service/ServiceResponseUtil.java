package com.saintgobain.dsi.starter.incapsula.service;

import org.apache.commons.lang3.StringUtils;

import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

public class ServiceResponseUtil {

    public static void checkWafResponse(IncapsulaResponse response) throws IncapsulaException {
        Integer responseCode = response.getRes();
        String responseMessage = response.getResMessage();

        if (!StringUtils.equalsIgnoreCase("OK", responseMessage)) {
            throw new BadRequestException(responseCode + ":" + responseMessage);
        }
    }

}
