package com.nchernov.trial.uc.services;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;

import java.util.Map;

public interface UrlMappingManager {
    UrlMapping findByPseudoHash(String pseudoHash);
    UrlMapping create(String originalUrl, Map<String, Object> context) throws CreationException;
}
