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

  @PostConstruct
  public void initializeMenuData() {
    log.info("애플리케이션 시작: 5주 전까지의 식단 크롤링 실행");
    menuService.getMenuItems(false);
    log.info("5주 전까지의 식단 크롤링 완료");
  }

  @Scheduled(fixedRate = 360000)
  public void scheduledMeal() {
    log.info("정기 스케줄: 이번 주 식단 크롤링 실행");
    menuService.getMenuItems(true);
    log.info("이번 주 식단 크롤링 완료");
  }
}