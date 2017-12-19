package com.nchernov.trial.uc.domain;

import java.util.Date;

public class UrlVisit {
    private String url;
    private String pseudoHash;
    private Date visitedAt;

    public String getUrl() {
        return url;
    }

    public String getPseudoHash() {
        return pseudoHash;
    }

    public Date getVisitedAt() {
        return visitedAt;
    }
}
