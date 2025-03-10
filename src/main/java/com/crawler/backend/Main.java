package com.crawler.backend;

import com.crawler.backend.controller.CrawlController;

public class Main {
    private static final String BASE_URL = "BASE_URL";
    private static final String BASE_URL_VALUE = System.getenv(BASE_URL);

    public static void main(String[] args) {
        CrawlController controller = new CrawlController(BASE_URL_VALUE);
        controller.setupEndpoints();
    }
}
