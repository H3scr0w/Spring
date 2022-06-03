package com.saintgobain.dsi.starter.incapsula.service;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.bean.Statistic;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatActions;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatRules;
import com.saintgobain.dsi.starter.incapsula.bean.TrafficVisit;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class TrafficService extends IncapsulaService {

    public TrafficService(RestTemplate incapsulaRestTemplate) {
        super(incapsulaRestTemplate);
    }

    public IncapsulaResponse getStats(String endpoint, String siteId, @Valid Statistic statistic, Pageable pageable)
            throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        uriComponentsBuilder.queryParam("time_range", statistic.getTimeRange().name());

        String statsParam = statistic.getStats().stream().map(s -> s.name()).collect(Collectors.joining(","));
        uriComponentsBuilder.queryParam("stats", statsParam);

        ResponseEntity<IncapsulaResponse> response = postRequestPaginated(uriComponentsBuilder, pageable);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

    public IncapsulaResponse getVisits(String endpoint, String siteId, TrafficVisit visit, Pageable pageable)
            throws IncapsulaException {

        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);

        if (visit.getTimeRange() != null) {
            uriComponentsBuilder.queryParam("time_range", visit.getTimeRange().name());
        }

        if (!CollectionUtils.isEmpty(visit.getSecurities())) {

            String securitiesParam = visit.getSecurities().stream().map((s) -> {

                if (s instanceof ThreatRules) {
                    ThreatRules rule = (ThreatRules) s;
                    return "api.threats." + rule.name();
                } else {
                    ThreatActions action = (ThreatActions) s;
                    return "api.threats.action." + action.name();
                }
            }).collect(Collectors.joining(
                    ","));

            uriComponentsBuilder.queryParam("security", securitiesParam);

        }

        if (!CollectionUtils.isEmpty(visit.getCountries())) {
            String countriesParam = visit.getCountries().stream().map(s -> s).collect(Collectors.joining(
                    ","));
            uriComponentsBuilder.queryParam("country", countriesParam);
        }

        if (!CollectionUtils.isEmpty(visit.getIps())) {
            String ipsParam = visit.getIps().stream().map(s -> s).collect(Collectors.joining(
                    ","));
            uriComponentsBuilder.queryParam("ip", ipsParam);
        }

        if (!CollectionUtils.isEmpty(visit.getVisitIds())) {
            String visitIdsParam = visit.getVisitIds().stream().map(s -> s).collect(Collectors.joining(
                    ","));
            uriComponentsBuilder.queryParam("visit_id", visitIdsParam);
        }

        if (visit.getListLiveVisits() != null) {
            uriComponentsBuilder.queryParam("list_live_visits", visit.getListLiveVisits());
        }

        ResponseEntity<IncapsulaResponse> response = postRequestPaginated(uriComponentsBuilder, pageable);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;
    }

}
