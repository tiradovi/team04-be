package com.example.crawler.meal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealSummaryDto {
    private String mealName;
    private String mealCategory;
}
