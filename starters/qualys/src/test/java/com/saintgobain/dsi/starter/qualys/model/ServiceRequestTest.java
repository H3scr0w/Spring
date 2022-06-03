package com.saintgobain.dsi.starter.qualys.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintgobain.dsi.starter.qualys.bean.Fields;
import com.saintgobain.dsi.starter.qualys.bean.ListWrapper;
import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.bean.SetWrapper;
import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.ServerRecord;
import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthRecord;
import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthServerRecordField;

@RunWith(SpringRunner.class)
public class ServiceRequestTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void createServiceRequestWithOneData() throws JsonProcessingException {

        Long timeoutErrorThreshold = new Long(10);
        Long unexpectedErrorThreshold = new Long(20);

        OptionProfile profile = OptionProfile.builder()
                .name("Test")
                .timeoutErrorThreshold(timeoutErrorThreshold)
                .unexpectedErrorThreshold(unexpectedErrorThreshold).build();

        Criteria criteria1 = Criteria.builder()
                .field("id")
                .operator("GREATER")
                .value("31550000")
                .build();

        Criteria criteria2 = Criteria.builder()
                .field("id")
                .operator("LESSER")
                .value("31650000")
                .build();

        Filters filters = Filters.builder().criteria(Arrays.asList(criteria1, criteria2)).build();

        Pageable pageable = PageRequest.of(0, 20);

        Preferences prefs = new Preferences();
        prefs.setLimitResults(pageable.getPageSize());
        prefs.setStartFromOffset(pageable.getOffset());

        ServiceRequest<OptionProfile> request = ServiceRequest.<OptionProfile> builder()
                .data(profile).filters(filters).preferences(prefs)
                .build();
        Assert.assertTrue(request.getData() instanceof OptionProfile);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestAsString = objectMapper.writeValueAsString(request);
        Assert.assertNotNull("Request Json", requestAsString);

    }

    @Test
    public void createServiceRequestWithWebAppAuth() throws JsonProcessingException {

        SetWrapper<WebAppAuthServerRecordField> set = SetWrapper.<WebAppAuthServerRecordField> builder()
                .webAppAuthServerRecordField(new HashSet<WebAppAuthServerRecordField>())
                .build();

        List<ListWrapper<WebAppAuthServerRecordField>> list = new ArrayList<>();

        Fields<WebAppAuthServerRecordField> fields = Fields.<WebAppAuthServerRecordField> builder()
                .set(set)
                .list(list)
                .build();

        ServerRecord serverRecord = ServerRecord.builder()
                .sslOnly(false)
                .fields(fields)
                .build();

        WebAppAuthRecord authRecord = WebAppAuthRecord.builder()
                .name("test")
                .serverRecord(serverRecord)
                .build();

        Criteria criteria1 = Criteria.builder()
                .field("id")
                .operator("GREATER")
                .value("31550000")
                .build();

        Criteria criteria2 = Criteria.builder()
                .field("id")
                .operator("LESSER")
                .value("31650000")
                .build();

        Filters filters = Filters.builder().criteria(Arrays.asList(criteria1, criteria2)).build();

        Pageable pageable = PageRequest.of(0, 20);

        Preferences prefs = new Preferences();
        prefs.setLimitResults(pageable.getPageSize());
        prefs.setStartFromOffset(pageable.getOffset());

        ServiceRequest<WebAppAuthRecord> request = ServiceRequest.<WebAppAuthRecord> builder()
                .data(authRecord).filters(filters).preferences(prefs)
                .build();
        Assert.assertTrue(request.getData() instanceof WebAppAuthRecord);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestAsString = objectMapper.writeValueAsString(request);
        Assert.assertNotNull("Request Json", requestAsString);

    }

}
