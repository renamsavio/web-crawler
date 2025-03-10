package com.crawler.backend.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.crawler.backend.config.AppConfig;

public class CrawlerUtils {

    public boolean containsSearchTerm(String html, String searchTerm) {
        Pattern pattern = Pattern.compile(searchTerm, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        return matcher.find();
    }

    public Set<String> extractLinks(String html, String baseUrl) {
        Set<String> links = new HashSet<>();
        // Encontra href tanto com aspas simples quanto duplas
        Pattern pattern = Pattern.compile("<a[^>]+href=['\"]([^'\"]+)['\"]", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);

        while (matcher.find()) {
            String link = matcher.group(1).trim();
            // Ignora links de âncora e javascript
            if (link.startsWith("#") || link.startsWith("javascript:")) {
                continue;
            }
            // Trata URLs relativas
            if (!link.startsWith("http")) {
                if (link.startsWith("/")) {
                    // URL absoluta em relação ao domínio
                    try {
                        URL url = new URL(baseUrl);
                        link = url.getProtocol() + "://" + url.getHost() + link;
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    // URL relativa
                    link = baseUrl + (baseUrl.endsWith("/") ? "" : "/") + link;
                }
            }
            links.add(link);
        }

        return links;
    }

    public String getHtml(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(AppConfig.getCrawlerConnectionTimeout()); // 30 segundos para conexão
        con.setReadTimeout(AppConfig.getCrawlerReadTimeout()); // 30 segundos para leitura

        try {
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return response.toString();
            }
        } catch (Exception e) {
            System.out.println("Timeout ou erro ao acessar " + url + ": " + e.getMessage());
        }
        return null;
    }
}
