package com.example.crawler.meal.component;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class MealTableExtractor {
    public Element extractWeeklyMenuTable(Document doc) {
        return doc.select("table").stream()
            .filter(t -> t.selectFirst("caption") != null &&
                         t.selectFirst("caption").text().contains("일주일간 식단 안내"))
            .findFirst()
            .orElse(null);
    }
}
