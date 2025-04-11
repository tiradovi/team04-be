package com.example.webscraper.meal.service;

import com.example.webscraper.meal.entity.MealItem;
import com.example.webscraper.meal.repository.MealItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MealItemService {

  private final MenuKafkaProducer menuKafkaProducer;
  private final MealItemCrawlerService mealItemCrawlerService;
  private final MealItemRepository mealItemRepository;

  public List<MealItem> getMealItems() {
    List<MealItem> menuList = mealItemCrawlerService.mealItemCrawler();
    if (menuList.isEmpty()) {
      throw new IllegalStateException("식단 데이터를 수집하지 못했습니다.");
    }

    // 저장된 ID 목록 조회
    List<String> existingIds = mealItemRepository.findAll().stream()
        .map(MealItem::generateId)
        .toList();

    // 새로 들어온 항목 중 중복 아닌 것만 필터링
    List<MealItem> newItems = menuList.stream()
        .filter(item -> !existingIds.contains(item.generateId()))
        .toList();

    if (newItems.isEmpty()) {
      log.info("저장할 새로운 식단 데이터 없음");
      return menuList;
    }

    List<MealItem> result = mealItemRepository.saveAll(newItems);
    menuKafkaProducer.sendAllMealItems(result);
    return result;
  }


  @Scheduled(fixedRate = 3600000)
  public void scheduledMeal() {
    log.info("식단 크롤링 스케줄 실행");
    getMealItems();
  }
}
