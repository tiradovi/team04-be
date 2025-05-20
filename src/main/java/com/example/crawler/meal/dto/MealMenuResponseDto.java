package com.example.crawler.meal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealMenuResponseDto {
    private Integer mealMenuId;
    private String menuItem;

    private String mealCategory;
    private String allergy;

    private Double calorie_kcal;
    private Double carb_g;
    private Double protein_g;
    private Double fat_g;
    private String foodWeight;
}
