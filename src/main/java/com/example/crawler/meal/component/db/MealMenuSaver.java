package com.example.crawler.meal.component.db;

import com.example.crawler.meal.component.crawling.NutriApiResponseApiClient;
import com.example.crawler.meal.dto.NutriApiResponseDto.Item;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.MealMenu;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.repository.MealMenuRepository;
import com.example.crawler.meal.repository.MealRepository;
import com.example.crawler.meal.repository.MenuRepository;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MealMenuSaver {

  private final MenuRepository menuRepository;
  private final MealRepository mealRepository;
  private final MealMenuRepository mealMenuRepository;
  private final MealCategory mealCategory;
  private final NutriApiResponseApiClient nutriApiResponseApiClient;


  public List<Menu> saveMenus(List<Menu> menus) {
    List<Integer> ids = menus.stream().map(Menu::getMenuId).toList();

    Set<Integer> existingIds = new HashSet<>(
        menuRepository.findAllById(ids).stream().map(Menu::getMenuId).toList());

    List<Menu> newMenus = menus.stream()
        .filter(menu -> !existingIds.contains(menu.getMenuId()))
        .toList();

    if (newMenus.isEmpty()) {
      return List.of();
    }

    List<Menu> savedMenus = menuRepository.saveAll(newMenus);
    savedMenus.forEach(this::saveMealRelations);

    return savedMenus;
  }

  private void saveMealRelations(Menu menu) {
    extractFoodNames(menu.getMenuContent()).forEach(foodName -> {
      Meal meal = findOrCreateMeal(foodName);
      meal.setMealCategory(mealCategory.classify(foodName));
      saveMealMenu(menu, meal);
    });
  }

  private List<String> extractFoodNames(String content) {
    if (content == null || content.isBlank()) {
      return List.of();
    }

    return Arrays.stream(content.split("\\s+"))
        .map(name -> name.replaceAll("[&/()\\[\\],]", "").trim())
        .filter(name -> name.length() > 1)
        .distinct()
        .collect(Collectors.toList());
  }

  private Meal findOrCreateMeal(String mealName) {
    return mealRepository.findByMealName(mealName)
        .orElseGet(() -> mealRepository.save(saveMeal(mealName)));
  }

  private Meal saveMeal(String mealName) {
    Meal meal = Meal.of(mealName);
    try {
      Item info = nutriApiResponseApiClient.fetchNutritionInfo(mealName);
      if (info != null) {
        meal.setCalorie_kcal(parseDoubleOrZero(info.getCalories()));
        meal.setCarb_g(parseDoubleOrZero(info.getCarb()));
        meal.setProtein_g(parseDoubleOrZero(info.getProtein()));
        meal.setFat_g(parseDoubleOrZero(info.getFat()));
        meal.setFoodWeight(info.getFoodWeight());
        return meal;
      }
    } catch (Exception ignored) {
    }

    setDefaultNutrition(meal);
    return meal;
  }

  private void setDefaultNutrition(Meal meal) {
    meal.setCalorie_kcal(0.0);
    meal.setCarb_g(0.0);
    meal.setProtein_g(0.0);
    meal.setFat_g(0.0);
    meal.setFoodWeight("0.0ml");
  }

  private Double parseDoubleOrZero(String value) {
    try {
      return (value == null || value.isBlank()) ? 0.0 : Double.parseDouble(value.trim());
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  private void saveMealMenu(Menu menu, Meal meal) {
    if (mealMenuRepository.findByMenuAndMeal(menu, meal).isEmpty()) {
      MealMenu mealMenu = new MealMenu(menu, meal);
      mealMenuRepository.save(mealMenu);
      menu.getMealMenus().add(mealMenu);
      meal.getMealMenus().add(mealMenu);
    }
  }
}
