package com.example.crawler.event.component.crawling;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class EventExtractor {

    public List<Element> extractEventItems(Document document) {
        Element tbody = document.selectFirst("tbody");
        if (tbody == null) {
            log.warn("tbody를 찾을 수 없음");
            return List.of();
        }
        Elements rows = tbody.select("tr");
        log.info("추출된 이벤트 행 개수: {}", rows.size());
        return rows;
    }
}
