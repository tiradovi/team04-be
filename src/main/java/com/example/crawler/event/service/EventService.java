package com.example.crawler.event.service;

import com.example.crawler.event.component.kafka.EventKafkaProducer;
import com.example.crawler.event.component.EventMapper;
import com.example.crawler.event.dto.EventResponseDto;
import com.example.crawler.event.entity.Event;
import jakarta.annotation.PostConstruct;
import java.util.Collections;
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
  private final EventSaveService eventSaveService;
  private final EventKafkaProducer eventKafkaProducer;

  public List<EventResponseDto> getEventItems() {
    List<Event> events = eventCrawlerService.crawlEvents();
    Collections.reverse(events);
    eventSaveService.saveEvents(events);

    eventKafkaProducer.sendAllEventItems(events);

    return events.stream()
        .map(eventMapper::toDto)
        .collect(Collectors.toList());
  }
}