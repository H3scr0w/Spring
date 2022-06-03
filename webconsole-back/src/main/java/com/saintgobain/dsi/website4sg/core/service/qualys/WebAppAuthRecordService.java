package com.saintgobain.dsi.website4sg.core.service.qualys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.bean.webappauthrecord.WebAppAuthRecord;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.service.RestService;

/**
 * The Class WebAppAuthRecordService.
 */
@Service
@Transactional
public class WebAppAuthRecordService extends RestService<WebAppAuthRecord> {

    public WebAppAuthRecordService(RestTemplate qualysRestTemplate) {
        super(qualysRestTemplate);
    }

    public WebAppAuthRecord create(WebAppAuthRecord webAppAuthRecord) throws QualysException {
        return create(webAppAuthRecord, "/create/was/webappauthrecord");
    }

    public WebAppAuthRecord read(Long id) throws QualysException {
        return read(id, "/get/was/webappauthrecord/");
    }

    public WebAppAuthRecord update(Long id, WebAppAuthRecord webAppAuthRecord) throws QualysException {

        webAppAuthRecord.setOwner(null);

        return update(id, webAppAuthRecord, "/update/was/webappauthrecord/");
    }

    public void delete(Long id) throws QualysException {
        delete(id, "/delete/was/webappauthrecord/");
    }

    public Page<WebAppAuthRecord> getAll(Pageable pageable) throws QualysException {
        return getAll(pageable, "/search/was/webappauthrecord", "/count/was/webappauthrecord");
    }

    public Page<WebAppAuthRecord> search(Pageable pageable, Filters filters) throws QualysException {
        return search(pageable, filters, "/search/was/webappauthrecord");
    }

}
