package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.domain.UrlVisitSummary;
import com.nchernov.trial.uc.services.dao.UrlVisitSummaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;

@Component
public class UrlVisitSummaryDaoImpl implements UrlVisitSummaryDao {

    @Autowired
    private EntityManager entityManager;

    String Q_GET_TOP_N_VISITED = "select visit_count, url, pseudo_hash\n" +
            "  FROM \n" +
            "(select count(*) as visit_count, uv.url_mapping_url_mapping_id AS url_mapping_id, url, pseudo_hash \n" +
            "  FROM url_visit AS uv\n" +
            "    JOIN url_mapping\n" +
            "  ON uv.url_mapping_url_mapping_id = url_mapping.url_mapping_id\n" +
            "  GROUP BY uv.url_mapping_url_mapping_id \n" +
            "  ORDER BY 1 DESC LIMIT %d)";

    @Override
    public Collection<UrlVisitSummary> getTopVisitedUrls(int count) {
        Query q = entityManager.createNativeQuery(String.format(Q_GET_TOP_N_VISITED, count), "UrlVisitSummaryResult");
        List<UrlVisitSummary> urlVisitSummaries = q.getResultList();
        return urlVisitSummaries;
    }
}
