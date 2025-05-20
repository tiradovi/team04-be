package com.example.crawler.meal.service;

import com.example.crawler.meal.component.kafka.MenuKafkaProducer;
import com.example.crawler.meal.component.MenuMapper;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.Menu;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MenuService {

  private final MenuKafkaProducer menuKafkaProducer;

  private final MealMenuCrawlerService mealMenuCrawlerService;
  private final MealMenuSaveService mealMenuSaveService;
  private final MenuMapper menuMapper;

  public List<MenuResponseDto> getMenuItems() {
    List<Menu> menuList = mealMenuCrawlerService.mealMenuCrawler();
    List<Menu> newMenus = mealMenuSaveService.saveDB(menuList);

    if (!newMenus.isEmpty()) {
      // menuKafkaProducer.sendAllMealItems(newMenus);
    }
    return menuList.stream()
        .map(menuMapper::toDto)
        .toList();
  }
}