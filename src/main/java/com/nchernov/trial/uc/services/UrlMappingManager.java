package com.nchernov.trial.uc.services;

import com.nchernov.trial.uc.domain.UrlMapping;
import com.nchernov.trial.uc.exceptions.urlmapping.CreationException;

public interface UrlMappingManager {
    UrlMapping findByPseudoHash(String pseudoHash);
    UrlMapping create(String originalUrl) throws CreationException;
    UrlMapping visit(String pseudoHash);
}
