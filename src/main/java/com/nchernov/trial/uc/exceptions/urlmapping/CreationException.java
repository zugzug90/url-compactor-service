package com.nchernov.trial.uc.exceptions.urlmapping;

public class CreationException extends Exception {
    public CreationException(String message) {
        super(message);
    }

    public CreationException() {
        super("Failed to generate shortened link");
    }
}
