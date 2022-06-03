package com.sgdbf.starter.logger.autoconfigure;

import com.sgdbf.common.threading.ContextCopier;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Propagate MDC context to the new Thread
 */
@Component
public class MDCCopier implements ContextCopier {

    private final Map<String, String> copyOfContextMap;

    @Autowired
    public MDCCopier() {
        copyOfContextMap = null;
    }

    private MDCCopier(final Map<String, String> copyOfContextMap){
        this.copyOfContextMap = copyOfContextMap;
    }

    @Override
    public ContextCopier copy() {
        return new MDCCopier(MDC.getCopyOfContextMap());
    }

    @Override
    public void apply() {
        if (this.copyOfContextMap != null){
            MDC.setContextMap(copyOfContextMap);
        }
    }

    @Override
    public void clean() {
        MDC.clear();
    }
}
