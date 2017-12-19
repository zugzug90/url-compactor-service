package com.nchernov.trial.uc.rest.dto;

public class CompactResultResponse extends DefaultResponse {
    private String resultUrl;

    public CompactResultResponse(boolean success, String error) {
        super(success, error);
    }

    public CompactResultResponse(String resultUrl) {
        super(true, null);
        this.resultUrl = resultUrl;
    }

    public String getResultUrl() {
        return resultUrl;
    }
}
