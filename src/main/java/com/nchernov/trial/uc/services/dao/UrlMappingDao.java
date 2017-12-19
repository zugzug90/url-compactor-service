package com.nchernov.trial.uc.services.dao;

import com.nchernov.trial.uc.domain.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlMappingDao extends JpaRepository<UrlMapping, Long> {

    UrlMapping findByPseudoHash(String pseudoHash);
}
