package com.example.crawler.event.service;

import com.example.crawler.event.component.crawling.EventExtractor;
import com.example.crawler.event.component.crawling.EventHtmlFetcher;
import com.example.crawler.event.component.crawling.EventParser;
import com.example.crawler.event.entity.Event;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class EventCrawlerService {

    private static final String BASE_URL = "https://www.mju.ac.kr/mjukr/255/subview.do";
    private static final int MAX_PAGES = 5;

    private final EventHtmlFetcher eventHtmlFetcher;
    private final EventExtractor eventExtractor;
    private final EventParser eventParser;

    public List<Event> crawlEvents() {
        List<Event> allEvents = new ArrayList<>();

        for (int page = 1; page <= MAX_PAGES; page++) {
            String url = BASE_URL + "?page=" + page;

            try {
                log.info("이벤트 크롤링 시작 - URL: {}", url);
                Document document = eventHtmlFetcher.fetch(url);
                List<Element> rows = eventExtractor.extractEventItems(document);
                List<Event> parsed = eventParser.parse(rows);
                allEvents.addAll(parsed);
                log.info("{}페이지 크롤링 성공 - {}건", page, parsed.size());
            } catch (IOException e) {
                log.error("{}페이지 크롤링 실패 - URL: {}", page, url, e);
            }
        }

        log.info("전체 이벤트 수집 완료 - 총 {}건", allEvents.size());
        return allEvents;
    }
}
