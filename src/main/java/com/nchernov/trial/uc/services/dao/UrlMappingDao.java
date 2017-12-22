package com.nchernov.trial.uc.services.dao;

import com.nchernov.trial.uc.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * DAO for persisting url mappings in some storage
 */
public interface UrlMappingDao extends JpaRepository<UrlMapping, Long> {

    UrlMapping save(UrlMapping urlMapping);

    UrlMapping findByPseudoHash(String pseudoHash);

    UrlMapping findByUrl(String url);
}
