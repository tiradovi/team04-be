package com.example.crawler.event.service;

import com.example.crawler.event.component.EventKafkaProducer;
import com.example.crawler.event.component.EventMapper;
import com.example.crawler.event.component.EventSaver;
import com.example.crawler.event.dto.EventResponseDto;
import com.example.crawler.event.entity.Event;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

  private final EventCrawlerService eventCrawlerService;
  private final EventMapper eventMapper;
  private final EventSaver eventSaver;
  private final EventKafkaProducer eventKafkaProducer;

  @PostConstruct
  public void init() {
    getEventItems();
  }

  public List<EventResponseDto> getEventItems() {
    List<Event> events = eventCrawlerService.crawlEvents();
    List<Event> newEvents = eventSaver.saveEvents(events);

    if (!newEvents.isEmpty()) {
      eventKafkaProducer.sendAllEventItems(newEvents);
    }

    return events.stream()
        .map(eventMapper::toDto)
        .collect(Collectors.toList());
  }
}