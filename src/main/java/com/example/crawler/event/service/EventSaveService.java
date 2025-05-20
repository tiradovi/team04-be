package com.example.crawler.event.service;

import com.example.crawler.event.entity.Event;
import com.example.crawler.event.repository.EventRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSaveService {

  private final EventRepository eventRepository;

  public void saveEvents(List<Event> events) {
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

        log.info("이벤트 저장 완료: {}", event.getTitle());
      } catch (Exception e) {
        log.error("이벤트 저장 중 오류 발생: {}", event.getTitle(), e);
      }
    }

  }
}