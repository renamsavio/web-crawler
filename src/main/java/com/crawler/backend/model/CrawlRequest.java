package com.crawler.backend.model;

public class CrawlRequest {

    private String keyword;

    public CrawlRequest(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }
}