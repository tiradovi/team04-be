package com.example.crawler.event.component.kafka;

import com.example.crawler.event.entity.Event;
import com.example.kafka_schemas.EventMenu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventKafkaProducer {

    private final KafkaTemplate<String, EventMenu> kafkaTemplate;

    public void sendAllEventItems(List<Event> events) {
        try {
            for (Event event : events) {
                EventMenu eventMenu = createEventMenu(event);
                ProducerRecord<String, EventMenu> record = new ProducerRecord<>(
                    "event.web.crawler.updated", "event.data", eventMenu
                );
                kafkaTemplate.send(record);
                log.info("Kafka 전송 성공 - eventMenu: {}", eventMenu);
            }
        } catch (Exception e) {
            log.error("Kafka 전송 실패", e);
        }
    }

    private EventMenu createEventMenu(Event event) {
        return new EventMenu(
            event.getTitle(),
            event.getDate().toString()
        );
    }
}
