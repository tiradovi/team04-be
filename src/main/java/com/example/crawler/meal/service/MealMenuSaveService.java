package com.example.crawler.meal.service;

import com.example.crawler.meal.component.db.SaveMeal;
import com.example.crawler.meal.component.db.SaveMealMenu;
import com.example.crawler.meal.component.db.SaveMenu;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.Menu;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealMenuSaveService {

  private final SaveMenu saveMenu;
  private final SaveMeal saveMeal;
  private final SaveMealMenu saveMealMenu;

  public List<Menu> saveDB(List<Menu> menus) {
    List<Menu> savedMenus = saveMenu.saveMenus(menus);

    for (Menu menu : savedMenus) {
      List<Meal> meals = saveMeal.saveMeals(menu);

      for (Meal meal : meals) {
        saveMealMenu.saveMealMenu(menu, meal);
      }
    }

    return savedMenus;
  }
}
