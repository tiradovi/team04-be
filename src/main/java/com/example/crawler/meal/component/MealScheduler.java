package com.example.crawler.meal.component;

import com.example.crawler.meal.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;


@Slf4j
@Component
@RequiredArgsConstructor
public class MealScheduler {

  private final MenuService menuService;

  @Scheduled(fixedRate = 360000)
  public void scheduledMeal() {
    menuService.getMenuItems();
  }
}