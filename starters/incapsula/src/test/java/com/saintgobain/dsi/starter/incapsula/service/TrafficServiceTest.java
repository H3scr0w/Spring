package com.saintgobain.dsi.starter.incapsula.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.incapsula.bean.Statistic;
import com.saintgobain.dsi.starter.incapsula.bean.StatsNames;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatActions;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatRules;
import com.saintgobain.dsi.starter.incapsula.bean.TimeRanges;
import com.saintgobain.dsi.starter.incapsula.bean.TrafficVisit;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@RunWith(SpringRunner.class)
public class TrafficServiceTest {

    @Mock
    private RestTemplate incapsulaRestTemplate;

    private TrafficService trafficService;

    @Before
    public void setUp() throws Exception {

        trafficService = new TrafficService(incapsulaRestTemplate);
    }

    @Test
    public void getStatsTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/stats/v1?site_id=41472615&time_range=last_7_days&stats=visits_timeseries,threats&page_size=20&page_num=0";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        Statistic statistic = Statistic.builder()
                .timeRange(TimeRanges.last_7_days)
                .stats(Arrays.asList(StatsNames.visits_timeseries, StatsNames.threats))
                .build();

        Pageable pageable = PageRequest.of(0, 20);

        IncapsulaResponse result = trafficService.getStats("/api/stats/v1", siteId, statistic, pageable);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");

    }

    @Test
    public void getVisitsTest() throws IncapsulaException {

        // Given

        String siteId = "41472615";
        String path = "/api/visits/v1?site_id=41472615&time_range=last_7_days&security=api.threats.sql_injection,api.threats.action.block_ip&page_size=20&page_num=0";

        ResponseEntity<IncapsulaResponse> mockWafResponse = new ResponseEntity<IncapsulaResponse>(IncapsulaResponse
                .builder().res(0)
                .resMessage(
                        "OK").build(), HttpStatus.OK);

        // When

        when(incapsulaRestTemplate.exchange(Mockito.eq(path), Mockito
                .eq(HttpMethod.POST), Mockito.eq(null),
                Mockito.eq(IncapsulaResponse.class))).thenReturn(mockWafResponse);

        TrafficVisit visit = TrafficVisit.builder()
                .timeRange(TimeRanges.last_7_days)
                .securities(Arrays.asList(ThreatRules.sql_injection, ThreatActions.block_ip))
                .build();

        Pageable pageable = PageRequest.of(0, 20);

        IncapsulaResponse result = trafficService.getVisits("/api/visits/v1", siteId, visit, pageable);

        // Then

        then(result)
                .as("Check response is not null")
                .isNotNull();

        then(result.getResMessage())
                .as("Check that response message is OK")
                .isEqualTo("OK");

    }
}
