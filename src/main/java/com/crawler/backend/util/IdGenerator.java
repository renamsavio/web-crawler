package com.crawler.backend.util;

import java.util.UUID;

import com.crawler.backend.config.AppConfig;

public class IdGenerator {
    public static String generateRandomCode() {
        return UUID.randomUUID().toString()
                .replaceAll("-", "")
                .substring(0, AppConfig.getIdLength())
                .toUpperCase();
    }
}
