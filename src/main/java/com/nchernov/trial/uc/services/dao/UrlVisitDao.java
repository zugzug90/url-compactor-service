package com.nchernov.trial.uc.services.dao;

import com.nchernov.trial.uc.domain.UrlVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface UrlVisitDao extends JpaRepository<UrlVisit, Long> {

    String Q_GET_ALL_VISITS = "select uv from UrlVisit uv join uv.urlMapping";

    @Query(Q_GET_ALL_VISITS)
    Collection<UrlVisit> getAllVisits();
}
