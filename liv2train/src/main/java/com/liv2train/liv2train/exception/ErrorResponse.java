package com.liv2train.liv2train.exception;

import java.time.Instant;
import java.util.List;

public class ErrorResponse {
    private Instant timestamp = Instant.now();
    private int status;
    private String error;
    private List<String> details;
    private String path;

    public ErrorResponse() {}

    public ErrorResponse(int status, String error, List<String> details, String path) {
        this.status = status; this.error = error; this.details = details; this.path = path;
    }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public List<String> getDetails() { return details; }
    public void setDetails(List<String> details) { this.details = details; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
