package com.nchernov.trial.uc.services;

import java.util.Map;

public interface UrlCompactor {
    String compact(String originalUrl, Map<String, Object> context);
}
