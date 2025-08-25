package com.example.crawler.meal.component;

import com.example.crawler.meal.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class MealScheduler {

  private final MenuService menuService;

  @Scheduled(cron = "0 0 1 * * mon")
  public void scheduledMeal() {
    menuService.getMenuItems();
 //   menuService.sendKafkaMessages();
  }
}