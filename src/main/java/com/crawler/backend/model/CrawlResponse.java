package com.crawler.backend.model;

import java.util.Set;

public class CrawlResponse {
    private String id;
    private String status;
    private Set<String> urls;

    private String errorMessage;

    // Construtor para resposta de erro
    public CrawlResponse(String message, String id) {
        this.errorMessage = message;
        this.id = id;
    }

    // Construtor para resposta do POST
    public CrawlResponse(String crawlId) {
        this.id = crawlId;
        this.status = null;
        this.urls = null;
    }

    // Construtor para resposta do GET
    public CrawlResponse(String id, String status, Set<String> urls) {
        this.id = id;
        this.status = status;
        this.urls = urls;
    }

    public boolean isError() {
        return errorMessage != null && !errorMessage.isEmpty();
    }

    public String getMessage() {
        return errorMessage;
    }

    public String getId() {
        return id;
    }
}