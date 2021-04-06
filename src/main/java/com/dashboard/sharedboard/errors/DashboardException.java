package com.dashboard.sharedboard.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

public class DashboardException extends ResponseStatusException {
    private final HttpHeaders headers;

    public DashboardException(HttpStatus status, HttpHeaders headers) {
        super(status);
        this.headers = headers;
    }

    public HttpHeaders getResponseHeaders() {
        return Objects.requireNonNullElse(headers, HttpHeaders.EMPTY);
    }

}
