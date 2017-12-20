package com.nchernov.trial.uc.services.dao;

import com.nchernov.trial.uc.domain.UrlVisit;
import com.nchernov.trial.uc.domain.UrlVisitSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface UrlVisitDao extends JpaRepository<UrlVisit, Long> {

    String Q_GET_ALL_VISITS = "select uv from UrlVisit uv join uv.urlMapping";
    String Q_GET_TOP_N_VISITED = "select url, pseudo_hash as pseudoHash from url_mapping, (select count(*) as visitCount, id AS url_visit_id FROM url_visit WHERE  GROUP BY id ORDER BY COUNT(*) DESC LIMIT ?1)";
    //TODO: finish this query: how to join tables using fk ?? Where do I get those fk ??

    @Query(Q_GET_ALL_VISITS)
    Collection<UrlVisit> getAllVisits();

    @Query(value = Q_GET_TOP_N_VISITED, nativeQuery = true)
    Collection<UrlVisitSummary> getTopVisitedUrls(int count);
}
