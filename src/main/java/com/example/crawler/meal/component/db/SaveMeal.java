package com.example.crawler.meal.component.db;

import com.example.crawler.meal.component.crawling.api.DetailNutritionApiClient;
import com.example.crawler.meal.component.crawling.api.MealCategory;
import com.example.crawler.meal.component.crawling.api.NutriApiResponseApiClient;
import com.example.crawler.meal.dto.NutriApiResponseDto.Item;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaveMeal {

  private final MealRepository mealRepository;
  private final NutriApiResponseApiClient nutriApiClient;
  private final DetailNutritionApiClient detailNutritionApiClient;
  private final MealCategory mealCategory;

  public List<Meal> saveMeals(Menu menu) {
    List<String> foodNames = extractFoodNames(menu.getMenuContent());

    return foodNames.stream().map(foodName ->
        mealRepository.findByMealName(foodName)
            .orElseGet(() -> {
              Meal meal = Meal.of(foodName);

              Item primaryInfo = nutriApiClient.fetchNutritionInfo(foodName);
              var detailInfo = detailNutritionApiClient.fetchDetailInfo(foodName);

              meal.setCalorie_kcal(parseDouble(
                  choose(primaryInfo != null ? primaryInfo.getCalories() : null,
                      detailInfo != null ? detailInfo.getCalorie_kcal() : null)));
              meal.setCarb_g(parseDouble(
                  choose(primaryInfo != null ? primaryInfo.getCarb() : null,
                      detailInfo != null ? detailInfo.getCarb_g() : null)));
              meal.setProtein_g(parseDouble(
                  choose(primaryInfo != null ? primaryInfo.getProtein() : null,
                      detailInfo != null ? detailInfo.getProtein_g() : null)));
              meal.setFat_g(parseDouble(
                  choose(primaryInfo != null ? primaryInfo.getFat() : null,
                      detailInfo != null ? detailInfo.getFat_g() : null)));
              meal.setFoodWeight(
                  chooseString(primaryInfo != null ? primaryInfo.getFoodWeight() : null,
                      detailInfo != null ? detailInfo.getFoodWeight() : null,
                      "0.0ml"));

              meal.setMealCategory(
                  chooseString(detailInfo != null ? detailInfo.getMealCategory() : null,
                      mealCategory.classify(foodName),
                      "UNKNOWN"));

              meal.setAllergy(
                  chooseString(detailInfo != null ? detailInfo.getAllergy() : null,
                      "UNKNOWN",
                      "UNKNOWN"));

              return mealRepository.save(meal);
            })
    ).toList();
  }

  private double parseDouble(String value) {
    try {
      return value != null ? Double.parseDouble(value) : 0.0;
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  private String choose(String primary, Double fallback) {
    if (primary != null && !primary.isBlank()) return primary;
    if (fallback != null) return String.valueOf(fallback);
    return "0.0";
  }

  private String chooseString(String primary, String fallback, String defaultVal) {
    if (primary != null && !primary.isBlank()) return primary;
    if (fallback != null && !fallback.isBlank()) return fallback;
    return defaultVal;
  }

  private List<String> extractFoodNames(String content) {
    if (content == null || content.isBlank()) {
      return List.of();
    }
    return Arrays.stream(content.split("\\s+"))
        .map(String::trim)
        .distinct()
        .collect(Collectors.toList());
  }
}
