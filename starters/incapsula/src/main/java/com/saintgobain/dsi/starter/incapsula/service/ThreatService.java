package com.saintgobain.dsi.starter.incapsula.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.saintgobain.dsi.starter.incapsula.bean.ActivationMode;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatActions;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatRules;
import com.saintgobain.dsi.starter.incapsula.bean.ThreatsConfiguration;
import com.saintgobain.dsi.starter.incapsula.exception.BadRequestException;
import com.saintgobain.dsi.starter.incapsula.exception.IncapsulaException;

@Service
public class ThreatService extends IncapsulaService {

    private final static String PREFIX_API_THREATS = "api.threats.";

    private final static String PREFIX_API_THREATS_ACTIONS = "api.threats.action.";

    private final static List<Integer> THRESHOLDS_ACCEPTED = Arrays.asList(10, 20, 50, 100, 200, 500, 750, 1000, 2000,
            3000, 4000, 5000);

    public ThreatService(RestTemplate incapsulaRestTemplate) {
        super(incapsulaRestTemplate);
    }

    public IncapsulaResponse configureSecurity(String endpoint, String siteId, @Valid ThreatsConfiguration conf)
            throws IncapsulaException {
        UriComponentsBuilder uriComponentsBuilder = initRequest(endpoint, siteId);
        uriComponentsBuilder = processFields(uriComponentsBuilder, conf);

        switch (conf.getRuleId()) {

        case backdoor:
            uriComponentsBuilder = configureBackDoor(uriComponentsBuilder, conf);
            break;

        case bot_access_control:
            uriComponentsBuilder = configureBotAccessControl(uriComponentsBuilder, conf);
            break;

        case ddos:
            uriComponentsBuilder = configureDdos(uriComponentsBuilder, conf);
            break;
        default:
            break;
        }

        ResponseEntity<IncapsulaResponse> response = postRequest(uriComponentsBuilder);

        IncapsulaResponse result = response.getBody();

        ServiceResponseUtil.checkWafResponse(result);

        return result;

    }

    private UriComponentsBuilder processFields(UriComponentsBuilder uriComponentsBuilder,
            ThreatsConfiguration conf) {

        ThreatRules rule = conf.getRuleId();
        ThreatActions action = conf.getSecurityRuleAction();

        uriComponentsBuilder.queryParam("rule_id", PREFIX_API_THREATS + rule.name());

        if (action != null) {
            uriComponentsBuilder.queryParam("security_rule_action", PREFIX_API_THREATS_ACTIONS + action.name());
        }

        return uriComponentsBuilder;

    }

    private UriComponentsBuilder configureDdos(UriComponentsBuilder uriComponentsBuilder, ThreatsConfiguration conf)
            throws IncapsulaException {

        ActivationMode mode = conf.getActivationMode();

        if (mode != null) {
            ThreatRules rule = conf.getRuleId();
            String activationMode = PREFIX_API_THREATS + rule.name() + ".activation_mode." + mode.name();
            uriComponentsBuilder.queryParam("activation_mode", activationMode);
        }

        Integer ddosTrafficThreshold = conf.getDdosTrafficThreshold();

        if (ddosTrafficThreshold != null) {
            if (THRESHOLDS_ACCEPTED.contains(ddosTrafficThreshold)) {
                uriComponentsBuilder.queryParam("ddos_traffic_threshold", ddosTrafficThreshold);

            } else {
                throw new BadRequestException("Bad ddosTrafficThreshold");
            }
        }

        return uriComponentsBuilder;
    }

    private UriComponentsBuilder configureBotAccessControl(UriComponentsBuilder uriComponentsBuilder,
            ThreatsConfiguration conf) {

        Boolean blockBadBots = conf.getBlockBadBots();

        if (blockBadBots != null) {
            uriComponentsBuilder.queryParam("block_bad_bots", blockBadBots);
        }

        Boolean challengeSuspectedBots = conf.getChallengeSuspectedBots();

        if (challengeSuspectedBots != null) {
            uriComponentsBuilder.queryParam("challenge_suspected_bots", challengeSuspectedBots);
        }

        return uriComponentsBuilder;
    }

    private UriComponentsBuilder configureBackDoor(UriComponentsBuilder uriComponentsBuilder,
            ThreatsConfiguration conf) {

        ThreatActions action = conf.getSecurityRuleAction();

        if (action != null) {
            List<String> urlsToRemove = conf.getQuarantinedUrls();

            if (StringUtils.equals(action.name(), ThreatActions.quarantine_url.name()) && !CollectionUtils.isEmpty(
                    urlsToRemove)) {
                String urlsParam = urlsToRemove.stream().map(s -> s).collect(Collectors.joining(","));
                uriComponentsBuilder.queryParam("quarantined_urls", urlsParam);
            }
        }

        return uriComponentsBuilder;
    }

}
