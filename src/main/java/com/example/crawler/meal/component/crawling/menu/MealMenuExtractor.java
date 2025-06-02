package com.example.crawler.meal.component.crawling.menu;

import java.util.Objects;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class MealMenuExtractor {
    public Element extractWeeklyMenu(Document menu) {
        return menu.select("table").stream()
            .filter(t -> t.selectFirst("caption") != null &&
                         Objects.requireNonNull(t.selectFirst("caption")).text().contains("일주일간 식단 안내"))
            .findFirst()
            .orElse(null);
    }
}
