package com.saintgobain.dsi.starter.qualys.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.saintgobain.dsi.starter.qualys.exception.QualysException;
import com.saintgobain.dsi.starter.qualys.model.Filters;
import com.saintgobain.dsi.starter.qualys.model.Preferences;
import com.saintgobain.dsi.starter.qualys.model.ServiceRequest;
import com.saintgobain.dsi.starter.qualys.model.ServiceResponse;

public class RestService<T> {

    protected final RestTemplate qualysRestTemplate;

    public RestService(RestTemplate qualysRestTemplate) {
        this.qualysRestTemplate = qualysRestTemplate;
    }

    protected T create(T data, String endpoint) throws QualysException {
        ServiceRequest<T> requestData = ServiceRequest.<T> builder()
                .data(data)
                .build();

        HttpEntity<ServiceRequest<T>> request = new HttpEntity<>(requestData);

        ServiceResponse<List<T>> response = this.qualysRestTemplate.exchange(endpoint,
                HttpMethod.POST, request, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        List<T> dataList = response.getData();

        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.get(0);
        }

        return null;
    }

    protected T read(Long id, String endpoint) throws QualysException {

        ServiceResponse<List<T>> response = qualysRestTemplate.exchange(
                endpoint + id,
                HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        List<T> dataList = response.getData();

        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.get(0);
        }

        return null;
    }

    protected T update(Long id, T data, String endpoint) throws QualysException {
        ServiceRequest<T> requestData = ServiceRequest.<T> builder()
                .data(data)
                .build();

        HttpEntity<ServiceRequest<T>> request = new HttpEntity<>(requestData);

        ServiceResponse<List<T>> response = qualysRestTemplate.exchange(endpoint
                + id,
                HttpMethod.POST, request, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        List<T> dataList = response.getData();

        if (!CollectionUtils.isEmpty(dataList)) {
            return dataList.get(0);
        }

        return null;
    }

    protected void delete(Long id, String endpoint) throws QualysException {
        ServiceResponse<List<T>> response = qualysRestTemplate.exchange(endpoint
                + id,
                HttpMethod.POST, null, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);
    }

    protected Page<T> getAll(Pageable pageable, String endpoint, String countEndpoint) throws QualysException {

        Preferences prefs = new Preferences();
        prefs.setLimitResults(pageable.getPageSize());
        prefs.setStartFromOffset(pageable.getOffset() + 1);

        ServiceRequest<T> requestData = ServiceRequest.<T> builder()
                .preferences(prefs)
                .build();

        HttpEntity<ServiceRequest<T>> request = new HttpEntity<>(requestData);

        ServiceResponse<List<T>> response = qualysRestTemplate.exchange(
                endpoint,
                HttpMethod.POST, request, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        List<T> dataList = response.getData();

        if (CollectionUtils.isEmpty(dataList)) {
            dataList = new ArrayList<T>();
        }

        ServiceResponse<T> countResponse = count(countEndpoint);

        Page<T> dataPage = new PageImpl<T>(dataList, pageable, countResponse.getCount());

        return dataPage;
    }

    protected Page<T> search(Pageable pageable, Filters filters, String endpoint) throws QualysException {

        Preferences prefs = new Preferences();
        prefs.setLimitResults(pageable.getPageSize());
        prefs.setStartFromOffset(pageable.getOffset() + 1);

        ServiceRequest<T> requestData = ServiceRequest.<T> builder().filters(
                filters)
                .preferences(prefs)
                .build();

        HttpEntity<ServiceRequest<T>> request = new HttpEntity<>(requestData);

        ServiceResponse<List<T>> response = qualysRestTemplate.exchange(
                endpoint,
                HttpMethod.POST, request, new ParameterizedTypeReference<ServiceResponse<List<T>>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        List<T> dataList = response.getData();

        if (CollectionUtils.isEmpty(dataList)) {
            dataList = new ArrayList<T>();
        }

        Page<T> dataPage = new PageImpl<T>(dataList, pageable, response.getCount());

        return dataPage;
    }

    private ServiceResponse<T> count(String endpoint) throws QualysException {
        ServiceResponse<T> response = qualysRestTemplate.exchange(
                endpoint,
                HttpMethod.GET, null, new ParameterizedTypeReference<ServiceResponse<T>>() {
                }).getBody();

        ServiceResponseUtil.checkResponse(response);

        return response;

    }

}
