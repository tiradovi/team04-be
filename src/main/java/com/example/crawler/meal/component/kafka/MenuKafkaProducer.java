package com.example.crawler.meal.component.kafka;

import com.example.crawler.meal.entity.Menu;
import com.example.kafka_schemas.MealEvent;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MenuKafkaProducer {

  private final KafkaTemplate<String, MealEvent> kafkaTemplate;

  public void sendAllMealItems(List<Menu> weekMenus) {
    try {
      for (Menu menu : weekMenus) {
        MealEvent mealEvent = createMealEvent(menu);
        ProducerRecord<String, MealEvent> producerRecord = new ProducerRecord<>(
            "meal.web.crawler.updated", "meal.data", mealEvent);
        kafkaTemplate.send(producerRecord);
        log.info("Kafka 전송 성공 - mealEvent: {}", mealEvent);
      }
    } catch (Exception e) {
      log.error("Kafka 전송 실패", e);
    }
  }

  private MealEvent createMealEvent(Menu menu) {
    return new MealEvent(menu.getMealType(), menu.getMenuContent(), menu.getDate().toString());
  }
}
