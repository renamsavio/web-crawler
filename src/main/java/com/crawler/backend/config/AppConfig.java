package com.crawler.backend.config;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = AppConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Error loading application.properties", e);
        }
    }

    public static int getKeywordMinLength() {
        return Integer.parseInt(properties.getProperty("keyword.min.length"));
    }

    public static int getKeywordMaxLength() {
        return Integer.parseInt(properties.getProperty("keyword.max.length"));
    }

    public static int getCrawlerConnectionTimeout() {
        return Integer.parseInt(properties.getProperty("crawler.connection.timeout"));
    }

    public static int getCrawlerReadTimeout() {
        return Integer.parseInt(properties.getProperty("crawler.read.timeout"));
    }

    public static int getIdLength() {
        return Integer.parseInt(properties.getProperty("id.length"));
    }
}
