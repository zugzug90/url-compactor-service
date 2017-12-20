package com.nchernov.trial.uc.services.impl;

import com.nchernov.trial.uc.domain.UrlVisitSummary;
import com.nchernov.trial.uc.services.UrlVisitAnalyticsService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Component
public class CachingUrlVisitAnalyticsService implements UrlVisitAnalyticsService {

    @Override
    public Collection<UrlVisitSummary> topVisited(int count) {
        return Collections.emptyList();
    }
}
