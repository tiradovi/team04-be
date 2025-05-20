package com.example.crawler.event.component.crawling;

import org.springframework.stereotype.Component;

@Component
public class EventFormatter {

    private static final String[] KEYWORDS = {"음악회", "행사", "대회"};

    public boolean isRelevant(String title) {
        for (String keyword : KEYWORDS) {
            if (title.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    public String extractCoreEventName(String originalTitle) {
        String title = originalTitle;

        title = title.replaceFirst("^\\[[^\\]]+\\]\\s*", "");

        while (title.matches(".*\\([^()]*\\).*")) {
            title = title.replaceAll("\\([^()]*\\)", "");
        }
        title = title.replaceAll("\\(.*?\\)", "");
        title = title.replaceAll("20\\d{2}(-\\d)?([년학도]*)?", "");
        title = title.replaceAll("(?i)(자원봉사자\\s*)?모집|발표|공지|참가|신청|안내|새글", "");

        return title.trim().replaceAll("\\s{2,}", " ");
    }
}