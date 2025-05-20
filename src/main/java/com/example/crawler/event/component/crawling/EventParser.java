package com.example.crawler.event.component.crawling;

import com.example.crawler.event.entity.Event;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
public class EventParser {

    private static final String BASE_URL = "https://www.mju.ac.kr";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final EventFormatter eventFormatter;

    public EventParser(EventFormatter eventFormatter) {
        this.eventFormatter = eventFormatter;
    }

    public List<Event> parse(List<Element> eventRows) {
        List<Event> events = new ArrayList<>();

        for (Element row : eventRows) {
            try {
                Element titleTd = row.selectFirst("td._artclTdTitle");
                Element dateTd = row.selectFirst("td._artclTdRdate");
                if (titleTd == null || dateTd == null) continue;

                Element linkElement = titleTd.selectFirst("a.artclLinkView");
                if (linkElement == null) continue;

                String rawTitle = linkElement.text().trim();

                if (!eventFormatter.isRelevant(rawTitle)) continue;

                String cleanedTitle = eventFormatter.extractCoreEventName(rawTitle);
                LocalDate date = LocalDate.parse(dateTd.text().trim(), DATE_FORMATTER);

                Event event = Event.builder()
                    .title(cleanedTitle)
                    .date(date)
                    .build();

                events.add(event);
            } catch (Exception e) {
                log.error("이벤트 파싱 중 오류 발생", e);
            }
        }
        return events;
    }

}
