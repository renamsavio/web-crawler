package com.crawler.backend.controller;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.crawler.backend.Crawler;
import com.crawler.backend.config.AppConfig;
import com.crawler.backend.enums.CrawlStatus;
import com.crawler.backend.model.CrawlRequest;
import com.crawler.backend.model.CrawlResponse;
import com.crawler.backend.util.CrawlerUtils;
import com.crawler.backend.util.IdGenerator;
import com.google.gson.Gson;

public class CrawlController {
    private final String baseUrl;
    private final Set<String> visitedUrls;
    private final Map<String, Set<String>> crawlResults;
    private final Map<String, CrawlStatus> crawlStatus;
    private final Gson gson;
    private final CrawlerUtils crawlerUtils;
    private String searchTerm;

    public CrawlController(String baseUrl) {
        this.baseUrl = baseUrl;
        this.visitedUrls = new HashSet<>();
        this.crawlResults = new ConcurrentHashMap<>();
        this.crawlStatus = new ConcurrentHashMap<>();
        this.gson = new Gson();
        this.crawlerUtils = new CrawlerUtils();
    }

    public void setupEndpoints() {
        setupGetEndpoint();
        setupPostEndpoint();
    }

    private void setupGetEndpoint() {
        get("/crawl/:id", (req, res) -> {
            String id = req.params("id");
            Set<String> results = crawlResults.get(id);

            if (results == null) {
                res.status(404);
                return gson.toJson(new CrawlResponse("Id not found", id));
            }

            String status = crawlStatus.get(id) == CrawlStatus.DONE ? "done" : "active";
            res.status(200);
            res.type("application/json");
            return gson.toJson(new CrawlResponse(id, status, results));
        });
    }

    private void setupPostEndpoint() {
        post("/crawl", (req, res) -> {
            System.out.println("POST /crawl" + System.lineSeparator() + req.body());
            CrawlRequest crawlRequest = gson.fromJson(req.body(), CrawlRequest.class);

            String keyword = crawlRequest.getKeyword();
            if (keyword == null ||
                    keyword.length() < AppConfig.getKeywordMinLength() ||
                    keyword.length() > AppConfig.getKeywordMaxLength()) {
                res.status(400);
                return gson.toJson(new CrawlResponse(
                        "keyword must be between " + AppConfig.getKeywordMinLength() +
                                " and " + AppConfig.getKeywordMaxLength() + " characters",
                        null));
            }

            String randomId = IdGenerator.generateRandomCode();
            searchTerm = keyword;

            crawlStatus.put(randomId, CrawlStatus.ACTIVE);

            new Thread(() -> {
                Set<String> results = new HashSet<>();
                crawlResults.put(randomId, results);
                try {
                    Crawler crawler = new Crawler(baseUrl, visitedUrls, searchTerm, crawlerUtils);
                    crawler.crawl(baseUrl, results);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                crawlStatus.put(randomId, CrawlStatus.DONE);
                visitedUrls.clear();
            }).start();

            res.type("application/json");
            return gson.toJson(new CrawlResponse(randomId));
        });
    }
}
