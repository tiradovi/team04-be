package com.example.crawler.meal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealResponseDto {
    private String foodName;
    private String mealCategory;
    private Double calorie_kcal;
    private Double carb_g;
    private Double protein_g;
    private Double fat_g;
    private String foodWeight;
    private String allergy;
}
