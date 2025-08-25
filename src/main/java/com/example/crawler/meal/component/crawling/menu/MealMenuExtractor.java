package com.example.crawler.meal.component.crawling.menu;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MealMenuExtractor {

    public Element extractWeeklyMenu(Document menu) {
        Element table = menu.select("table").stream()
            .filter(t -> {
                Element caption = t.selectFirst("caption");
                return caption != null && caption.text().contains("일주일간 식단 안내");
            })
            .findFirst()
            .orElse(null);

        if (table == null) {
            log.warn("caption으로 테이블을 찾을 수 없음. 헤더로 검색 시도...");
            table = menu.select("table").stream()
                .filter(t -> {
                    Element thead = t.selectFirst("thead");
                    if (thead == null) return false;

                    String headerText = thead.text();
                    return headerText.contains("요일") &&
                        headerText.contains("식단구분") &&
                        headerText.contains("식단내용");
                })
                .findFirst()
                .orElse(null);
        }

        if (table == null) {
            log.warn("헤더로도 테이블을 찾을 수 없음. 내용으로 검색 시도...");
            table = menu.select("table").stream()
                .filter(t -> {
                    String tableText = t.text();
                    return (tableText.contains("조식") || tableText.contains("중식") || tableText.contains("석식")) &&
                        (tableText.contains("쌀밥") || tableText.contains("국") || tableText.contains("김치"));
                })
                .findFirst()
                .orElse(null);
        }

        if (table == null) {
            log.error("식단 테이블을 찾을 수 없습니다. HTML 구조를 확인해주세요.");
            log.debug("HTML 내용: {}", menu.html());
        } else {
            log.info("식단 테이블을 성공적으로 찾았습니다.");
        }

        return table;
    }
}