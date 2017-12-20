package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.domain.UrlVisitSummary;
import com.nchernov.trial.uc.services.UrlVisitAnalyticsService;
import com.nchernov.trial.uc.services.dao.UrlVisitDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CachingUrlVisitAnalyticsService implements UrlVisitAnalyticsService {

    private UrlVisitDao urlVisitDao;

    public CachingUrlVisitAnalyticsService(@Autowired UrlVisitDao urlVisitDao) {
        this.urlVisitDao = urlVisitDao;
    }

    @Override
    public Collection<UrlVisitSummary> topVisited(int count) {
        return urlVisitDao.getTopVisitedUrls(count);
    }
}
