package com.nchernov.trial.uc.domain;

public class UrlVisitSummary {
    private int visitCount;
    private String url;
    private String pseudoHash;

    public int getVisitCount() {
        return visitCount;
    }

    public String getUrl() {
        return url;
    }

    public String getPseudoHash() {
        return pseudoHash;
    }
}
