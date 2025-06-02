package com.example.crawler.meal.component.kafka;

import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.entity.MealMenu;
import com.example.kafka_schemas.WeekCategoryEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WeekCategoryKafkaProducer {

  private final KafkaTemplate<String, WeekCategoryEvent> kafkaTemplate;
  private static final String TOPIC = "meal.weekcategory.updated";
  private static final String KEY = "week.category.key";

  public void sendAllWeekCategoryItems(List<Menu> weekMenus) {
    weekMenus.forEach(menu -> {
      String date = menu.getDate().toString();
      String mealType = menu.getMealType();

      menu.getMealMenus().stream()
          .map(MealMenu::getMeal)
          .filter(meal -> meal != null)
          .map(meal -> createWeekCategoryEvent(mealType, meal.getMealName(), meal.getMealCategory(), date))
          .forEach(event -> {
            log.info("전송할 WeekCategoryEvent 생성: {}", event);
            kafkaTemplate.send(new ProducerRecord<>(TOPIC, KEY, event));
            log.info("Kafka 전송 성공 - WeekCategoryEvent: {}", event);
          });
    });
  }

  private WeekCategoryEvent createWeekCategoryEvent(String mealType, String mealName, String mealCategory, String date) {
    return new WeekCategoryEvent(mealType, mealName, mealCategory, date);
  }
}
