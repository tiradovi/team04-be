package com.example.crawler.meal.service;

import com.example.crawler.meal.dto.MealResponseDto;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MealService {

    private final MealRepository mealRepository;

    public MealResponseDto getNutritionByMealName(String mealName) {
        Meal meal = mealRepository.findByMealName(mealName)
            .orElseThrow(() -> new RuntimeException("해당 음식의 영양 정보를 찾을 수 없습니다."));

        return MealResponseDto.builder()
            .foodName(meal.getMealName())
            .mealCategory(meal.getMealCategory())
            .calorie_kcal(meal.getCalorie_kcal())
            .carb_g(meal.getCarb_g())
            .protein_g(meal.getProtein_g())
            .fat_g(meal.getFat_g())
            .foodWeight(meal.getFoodWeight())
            .allergy(meal.getAllergy())
            .build();
    }
}
