package com.example.crawler.meal.component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
public class MealItemFormatter {

  public String formatId(String mealType, LocalDate date) {
    String digit = switch (mealType) {
      case "breakfast" -> "1";
      case "lunch" -> "2";
      case "dinner" -> "3";
      default -> "0";
    };
    return date.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + digit;
  }


  public LocalDate formatDate(String rawDay) {
    String[] parts = rawDay.split(" ")[0].split("\\.");
    return LocalDate.of(
        LocalDate.now().getYear(),
        Integer.parseInt(parts[0]),
        Integer.parseInt(parts[1])
    );
  }


  public String formatMealType(String rawText) {
    return switch (rawText.trim()) {
      case "조식" -> "breakfast";
      case "중식" -> "lunch";
      case "석식" -> "dinner";
      default -> "unknown";
    };
  }

  public String formatMenuContent(String rawContent) {
    return rawContent
        .replaceAll("[\\u2600-\\u26FF]", "")
        .replaceAll("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]", "")
        .replaceAll("[♥★♡☺]", "")
        .replaceAll("\\[.*?]", "")
        .trim();
  }
}
