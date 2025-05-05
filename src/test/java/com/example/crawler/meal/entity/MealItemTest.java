package com.example.crawler.meal.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MealItem Entity Test")
public class MealItemTest {

  @Test
  @DisplayName("MealItem.of() 메서드로 객체를 생성할 수 있다")
  void MealItem_of_메서드_검중() {
    // given
    int id = 1;
    LocalDate day = LocalDate.of(2025, 5, 5);
    String mealType = "Lunch";
    String title = "Today’s Special";
    String content = "Chicken Curry, Rice";
    String extra = "Includes dessert";

    // when
    MealItem mealItem = MealItem.of(id, day, mealType, title, content, extra);

    // then
    assertThat(mealItem).isNotNull();
    assertThat(mealItem.getId()).isEqualTo(id);
    assertThat(mealItem.getDay()).isEqualTo(day);
    assertThat(mealItem.getMealType()).isEqualTo(mealType);
    assertThat(mealItem.getMenuTitle()).isEqualTo(title);
    assertThat(mealItem.getMenuContent()).isEqualTo(content);
    assertThat(mealItem.getExtraInfo()).isEqualTo(extra);
  }
}
