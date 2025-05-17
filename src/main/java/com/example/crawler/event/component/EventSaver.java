package com.example.crawler.event.component;

import com.example.crawler.event.entity.Event;
import com.example.crawler.event.repository.EventRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSaver {

  private final EventRepository eventRepository;

  public List<Event> saveEvents(List<Event> events) {
    List<Event> newEvents = new ArrayList<>();

    for (Event event : events) {
      try {
        boolean exists = eventRepository.findByTitleAndDate(
            event.getTitle(), event.getDate()
        ).isPresent();

        if (exists) {
          log.info("중복 이벤트 저장 생략: {}", event.getTitle());
          continue;
        }

        eventRepository.save(event);
        newEvents.add(event);
        log.info("이벤트 저장 완료: {}", event.getTitle());
      } catch (Exception e) {
        log.error("이벤트 저장 중 오류 발생: {}", event.getTitle(), e);
      }
    }

    return newEvents;
  }
}