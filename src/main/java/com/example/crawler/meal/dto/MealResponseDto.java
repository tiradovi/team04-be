package com.example.crawler.meal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealResponseDto {
    private Integer mealId;
    private String mealName;
    private String mealCategory;
    private String nutrition;
    private Integer calorie;
    private String allergy;
}
