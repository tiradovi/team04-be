package com.example.crawler.meal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class WeeklyMealInfoDto {
    private LocalDate date;
    private String mealType;
    private String mealCategory;
    private String mealName;
}
