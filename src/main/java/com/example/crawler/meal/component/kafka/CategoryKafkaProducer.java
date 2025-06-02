package com.example.crawler.meal.component.kafka;

import com.example.crawler.meal.entity.Meal;
import com.example.kafka_schemas.CategoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CategoryKafkaProducer {

  private final KafkaTemplate<String, CategoryEvent> kafkaTemplate;

  public void sendAllCategoryItem(List<Meal> allMeals) {
    try {
      for (Meal meal : allMeals) {
        CategoryEvent event = createCategoryEvent(meal.getMealName(), meal.getMealCategory());
        ProducerRecord<String, CategoryEvent> producerRecord = new ProducerRecord<>(
            "meal.category.updated", "category.key", event);
        kafkaTemplate.send(producerRecord);
        log.info("Kafka 전송 성공 - CategoryEvent: {}", event);
      }
    } catch (Exception e) {
      log.error("Kafka 전송 실패", e);
    }
  }

  public CategoryEvent createCategoryEvent(String mealName, String mealCategory) {
    return new CategoryEvent(mealName, mealCategory);
  }
}
