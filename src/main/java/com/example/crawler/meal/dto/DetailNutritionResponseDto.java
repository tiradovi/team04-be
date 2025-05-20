package com.example.crawler.meal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailNutritionResponseDto {

  private String food_name;
  private String mealCategory;
  private Double calorie_kcal;
  private Double carb_g;
  private Double protein_g;
  private Double fat_g;
  private String foodWeight;
  private String allergy;

}
