package com.example.crawler.meal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MealItem {

  @Id
  private int id;
  private LocalDate day;
  private String mealType;
  private String menuContent;
  private String menuTitle;
  private String extraInfo;

  public static MealItem of(int id, LocalDate day, String mealType, String title, String content,
      String extra) {
    return MealItem.builder()
        .id(id)
        .day(day)
        .mealType(mealType)
        .menuTitle(title)
        .menuContent(content)
        .extraInfo(extra)
        .build();
  }
}