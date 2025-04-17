package com.example.crawler.meal.component;

import com.example.crawler.meal.service.MealItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MealScheduler {

  private final MealItemService mealItemService;

  @Scheduled(fixedRate = 3600000)
  public void scheduledMeal() {
    log.info("식단 크롤링 스케줄 실행");
    mealItemService.getMealItems();
  }
}
