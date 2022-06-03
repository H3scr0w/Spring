package com.saintgobain.dsi.starter.incapsula.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;

@ApiModel(value = "ThreatRules", description = "ThreatRules Object")
@JsonIgnoreProperties(ignoreUnknown = true)
public enum ThreatRules implements Securities {

    // To precede with "api.threats." before call

    // no actions
    bot_access_control,

    // all actions except: quarantine_url
    sql_injection,

    // all actions except: quarantine_url
    cross_site_scripting,

    // all actions except: quarantine_url
    illegal_resource_access,

    // only actions: disabled, alert, quarantine_url
    backdoor,

    // no actions
    ddos,

    // all actions except: quarantine_url
    remote_file_inclusion
}
