package com.example.crawler.meal.service;

import com.example.crawler.meal.component.MenuKafkaProducer;
import com.example.crawler.meal.entity.MealItem;
import com.example.crawler.meal.repository.MealItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MealItemService {

  private final MenuKafkaProducer menuKafkaProducer;
  private final MealTableCrawlerService mealTableCrawlerService;
  private final MealItemRepository mealItemRepository;

  // TODO: 리팩토링
  public List<MealItem> getMealItems() {
    List<MealItem> menuList = mealTableCrawlerService.mealTableCrawler();

    List<Integer> existingIds = mealItemRepository.findAllIds();

    List<MealItem> newItems = menuList.stream()
        .filter(item -> !existingIds.contains(item.getId()))
        .toList();

    if (!newItems.isEmpty()) {
      mealItemRepository.saveAll(newItems);
      //menuKafkaProducer.sendAllMealItems(newItems);
    }

    return menuList;
  }
}
