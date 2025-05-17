package com.example.crawler.event.controller;

import com.example.crawler.event.dto.EventResponseDto;
import com.example.crawler.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team4/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/items")
    public ResponseEntity<List<EventResponseDto>> getEventItems() {
        return ResponseEntity.ok(eventService.getEventItems());
    }
}
