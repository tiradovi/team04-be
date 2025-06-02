package com.example.crawler.event.component;

import com.example.crawler.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventService eventService;


    @Scheduled(cron = "0 0 1 * * *")
    public void scheduledEventCrawling() {
        log.info("이벤트 크롤링 스케줄 실행");
        eventService.getEventItems();
    }
}
