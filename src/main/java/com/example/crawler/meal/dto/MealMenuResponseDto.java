package com.example.crawler.meal.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MealMenuResponseDto {
    private Integer mealMenuId;
    private String menuItem;
}
