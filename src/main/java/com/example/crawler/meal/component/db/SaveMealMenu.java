package com.example.crawler.meal.component.db;

import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.MealMenu;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.repository.MealMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveMealMenu {

  private final MealMenuRepository mealMenuRepository;

  public void saveMealMenu(Menu menu, Meal meal) {
    if (mealMenuRepository.findByMenuAndMeal(menu, meal).isEmpty()) {
      MealMenu mealMenu = new MealMenu(menu, meal);
      mealMenuRepository.save(mealMenu);
      menu.getMealMenus().add(mealMenu);
      meal.getMealMenus().add(mealMenu);
    }
  }
}
