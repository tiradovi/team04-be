package com.example.crawler.event.component;

import com.example.crawler.event.dto.EventResponseDto;
import com.example.crawler.event.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public EventResponseDto toDto(Event event) {
        return new EventResponseDto(
                event.getTitle(),
                event.getDate() != null ? event.getDate().toString() : null

        );
    }
}
