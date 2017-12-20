package com.nchernov.trial.uc.domain;

public interface ShortLinkAware {
    String getShortLink();
    String getPseudoHash();
    void setShortLink(String shortLink);
}
