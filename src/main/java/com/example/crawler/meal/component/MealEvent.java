package com.example.crawler.meal.component;

import lombok.Data;

@Data
public class MealEvent {

  private String mealType;
  private String mealContents;
  private String date;

  // 생성자
  public MealEvent(String mealType, String mealContents, String date) {
    this.mealType = mealType;
    this.mealContents = mealContents;
    this.date = date;
  }

  public MealEvent() {
    // 기본 생성자
  }
}
