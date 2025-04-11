package com.example.webscraper.meal.service;

import com.example.webscraper.meal.entity.MealItem;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuKafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic = "meal.web-scraper.updated";

    public void sendMenu(MealItem item) {
        String key = item.generateId();
        String payload = item.toMealJson();

//      kafkaTemplate.send(topic, key, payload);
//      log.info("Kafka 전송 성공 - key: {}, payload: {}", key, payload);
    }

    public void sendAllMealItems(List<MealItem> items) {
        for (MealItem item : items) {
            sendMenu(item);
        }
        log.info("Kafka 전송 성공 - key: {}, payload: {}", items.get(0).generateId(), items.get(0).toMealJson());
    }
}
