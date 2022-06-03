package com.saintgobain.dsi.website4sg.core.service.qualys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.bean.OptionProfile;
import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.service.RestService;

@Service
@Transactional
public class OptionProfileService extends RestService<OptionProfile> {

    public OptionProfileService(RestTemplate qualysRestTemplate) {
        super(qualysRestTemplate);
    }

    public Page<OptionProfile> search(Pageable pageable, Filters filters) throws QualysException {
        return search(pageable, filters, "/search/was/optionprofile");
    }

}
