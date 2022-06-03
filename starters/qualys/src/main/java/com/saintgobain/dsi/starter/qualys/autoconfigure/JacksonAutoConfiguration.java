package com.saintgobain.dsi.starter.qualys.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintgobain.dsi.starter.qualys.bean.Fields;
import com.saintgobain.dsi.starter.qualys.bean.ListWrapper;
import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.bean.SetWrapper;
import com.saintgobain.dsi.starter.qualys.bean.report.ConfigReport;
import com.saintgobain.dsi.starter.qualys.bean.report.Report;
import com.saintgobain.dsi.starter.qualys.bean.report.ScanReport;
import com.saintgobain.dsi.starter.qualys.bean.report.ScanWrapper;
import com.saintgobain.dsi.starter.qualys.bean.report.TargetReport;
import com.saintgobain.dsi.starter.qualys.bean.wasscan.WasScan;
import com.saintgobain.dsi.starter.qualys.bean.webapp.AuthRecordsWrapper;
import com.saintgobain.dsi.starter.qualys.bean.webapp.WebApp;
import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthRecord;
import com.saintgobain.dsi.starter.qualys.model.Criteria;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.model.Preferences;

@Configuration
public class JacksonAutoConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerSubtypes(WebAppAuthRecord.class, WasScan.class, Report.class, WebApp.class,
                OptionProfile.class, ScanWrapper.class,
                AuthRecordsWrapper.class, ListWrapper.class, SetWrapper.class, Fields.class, Criteria.class,
                Filters.class, Preferences.class, ConfigReport.class, ScanReport.class, TargetReport.class);
        objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        return objectMapper;
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJacksonMessageConverter() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper());
        return converter;
    }

}
