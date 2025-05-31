package com.example.crawler.meal.service;

import com.example.crawler.meal.component.kafka.CategoryKafkaProducer;
import com.example.crawler.meal.component.kafka.MenuKafkaProducer;
import com.example.crawler.meal.component.MenuMapper;
import com.example.crawler.meal.component.kafka.WeekCategoryKafkaProducer;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.repository.MealRepository;

import com.example.crawler.meal.repository.MenuRepository;
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
  private final CategoryKafkaProducer categoryKafkaProducer;
  private final WeekCategoryKafkaProducer weekCategoryKafkaProducer;

  private final MealRepository mealRepository;
  private final MenuRepository menuRepository;
  private final MealMenuCrawlerService mealMenuCrawlerService;
  private final MealMenuSaveService mealMenuSaveService;
  private final MenuMapper menuMapper;

  public List<MenuResponseDto> getMenuItems() {
    List<Menu> fullmenuList = mealMenuCrawlerService.mealMenuCrawler(false);
    mealMenuSaveService.saveDB(fullmenuList);
    List<Menu> fullMenusWithMealMenus = menuRepository.findAllWithMealMenus(fullmenuList);
    return fullMenusWithMealMenus.stream()
        .map(menuMapper::toDto)
        .toList();
  }

  public void sendKafkaMessages() {
    List<Menu> weekmenuList = mealMenuCrawlerService.mealMenuCrawler(true);
    List<Menu> weekMenusWithMeals = menuRepository.findAllWithMealMenus(weekmenuList);
    List<Meal> mealList = mealRepository.findAll();

    categoryKafkaProducer.sendAllCategoryItem(mealList);
    menuKafkaProducer.sendAllMealItems(weekmenuList);
    weekCategoryKafkaProducer.sendAllWeekCategoryItems(weekMenusWithMeals);
  }
}