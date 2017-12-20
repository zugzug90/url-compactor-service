package com.nchernov.trial.uc.services;

import com.nchernov.trial.uc.domain.UrlVisitSummary;

import java.util.Collection;

public interface UrlVisitAnalyticsService {
    Collection<UrlVisitSummary> topVisited(int count);
}
