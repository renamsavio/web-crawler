package com.crawler.backend;

import java.io.IOException;
import java.util.Set;

import com.crawler.backend.util.CrawlerUtils;

public class Crawler {
    private final String baseUrl;
    private final Set<String> visitedUrls;
    private final String searchTerm;
    private final CrawlerUtils crawlerUtils;

    public Crawler(String baseUrl, Set<String> visitedUrls, String searchTerm, CrawlerUtils crawlerUtils) {
        this.baseUrl = baseUrl;
        this.visitedUrls = visitedUrls;
        this.searchTerm = searchTerm;
        this.crawlerUtils = crawlerUtils;
    }

    public void crawl(String url, Set<String> results) throws IOException {
        if (!url.startsWith("http://") && !url.startsWith("https://") ||
                !url.startsWith(baseUrl)) {
            System.out.println("URL fora do escopo ou inválida: " + url);
            return;
        }

        if (visitedUrls.contains(url)) {
            return;
        }

        visitedUrls.add(url);
        System.out.println("Acessando: " + url);

        try {
            String html = crawlerUtils.getHtml(url);

            if (html != null) {
                if (crawlerUtils.containsSearchTerm(html, searchTerm)) {
                    System.out.println("URL contém o termo de busca: " + url);
                    results.add(url);
                }

                Set<String> links = crawlerUtils.extractLinks(html, url);
                for (String link : links) {
                    try {
                        crawl(link, results);
                    } catch (Exception e) {
                        System.out.println("Erro ao processar link " + link + ": " + e.getMessage());
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao acessar URL " + url + ": " + e.getMessage());
        }
    }
}
