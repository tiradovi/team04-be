package com.example.crawler.meal.component.db;

import com.example.crawler.meal.component.crawling.DetailNutritionApiClient;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.repository.MealRepository;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveMeal {

  private final MealRepository mealRepository;
  private final DetailNutritionApiClient detailNutritionApiClient;

  public List<Meal> saveMeals(Menu menu) {
    List<String> foodNames = extractFoodNames(menu.getMenuContent());

    return foodNames.stream().map(foodName ->
        mealRepository.findByMealName(foodName)
            .orElseGet(() -> {
              Meal food = Meal.of(foodName);

              var detailInfo = detailNutritionApiClient.fetchDetailInfo(foodName);
              if (detailInfo != null) {
                food.setCalorie_kcal(detailInfo.getCalorie_kcal() != null ? detailInfo.getCalorie_kcal() : 0.0);
                food.setCarb_g(detailInfo.getCarb_g() != null ? detailInfo.getCarb_g() : 0.0);
                food.setProtein_g(detailInfo.getProtein_g() != null ? detailInfo.getProtein_g() : 0.0);
                food.setFat_g(detailInfo.getFat_g() != null ? detailInfo.getFat_g() : 0.0);
                food.setFoodWeight(detailInfo.getFoodWeight() != null ? detailInfo.getFoodWeight() : "0.0ml");
                food.setMealCategory(detailInfo.getMealCategory() != null ? detailInfo.getMealCategory() : "UNKNOWN");
                food.setAllergy(detailInfo.getAllergy() != null ? detailInfo.getAllergy() : "UNKNOWN");
              } else {
                setDefaults(food);
              }
              return mealRepository.save(food);
            })
    ).toList();
  }

  private List<String> extractFoodNames(String content) {
    if (content == null || content.isBlank()) {
      return List.of();
    }
    return Arrays.stream(content.split("\\s+"))
        .map(String::trim)
        .filter(name -> name.length() > 1)
        .distinct()
        .collect(Collectors.toList());
  }

  private void setDefaults(Meal meal) {
    meal.setCalorie_kcal(0.0);
    meal.setCarb_g(0.0);
    meal.setProtein_g(0.0);
    meal.setFat_g(0.0);
    meal.setFoodWeight("0.0ml");
    meal.setMealCategory("UNKNOWN");
    meal.setAllergy(List.of().toString());
  }
}
