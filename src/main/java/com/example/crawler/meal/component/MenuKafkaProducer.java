package com.example.crawler.meal.component;


import com.example.crawler.meal.entity.MealItem;
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

  public void sendAllMealItems(List<MealItem> items) {
    try {
      for (MealItem item : items) {
        MealEvent mealEvent = createMealEventFromItem(item);
        ProducerRecord<String, MealEvent> producerRecord = new ProducerRecord<>(
            "meal.web.crawler.updated", "meal.data", mealEvent);
        kafkaTemplate.send(producerRecord);
        log.info("Kafka 전송 성공 - mealEvent: {}", mealEvent);
      }
    } catch (Exception e) {
      log.error("Kafka 전송 실패", e);
    }
  }

  private MealEvent createMealEventFromItem(MealItem item) {
    return new MealEvent(item.getMealType(), item.getMenuContent(), item.getDay().toString());
  }
}
