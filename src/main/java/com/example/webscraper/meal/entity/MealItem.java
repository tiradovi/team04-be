package com.example.webscraper.meal.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Element;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class MealItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String day;
  private String mealType;
  private String menuTitle;
  private String menuContent;
  private String extraInfo;

  public static MealItem of(String day, Element mealType, Element title, Element content, Element extra) {
    return MealItem.builder()
        .day(day)
        .mealType(mealType.text().trim())
        .menuTitle(title.text().trim())
        .menuContent(content.text().trim())
        .extraInfo(extra.text().trim())
        .build();
  }


  public String getFormattedDate() {
    String datePart = day.split(" ")[0];
    String[] parts = datePart.split("\\.");
    int year = LocalDate.now().getYear();
    return String.format("%d-%s-%s", year, parts[0], parts[1]);
  }

  public String generateId() {
    String digit = switch (mealType) {
      case "조식" -> "1";
      case "중식" -> "2";
      case "석식" -> "3";
      default -> "0";
    };
    return getFormattedDate().replaceAll("-", "") + digit;
  }

  public String getFullContents() {
    return String.join(" ", menuTitle, menuContent, extraInfo).trim();
  }


  public String toMealJson() {
    ObjectMapper mapper = new ObjectMapper();
    Map<String, Object> jsonMap = new HashMap<>();

    jsonMap.put("id", Long.parseLong(generateId()));
    jsonMap.put("dayInfo", getFormattedDate());
    jsonMap.put("mealType", switch (mealType) {
      case "조식" -> "BREAKFAST";
      case "중식" -> "LUNCH";
      case "석식" -> "DINNER";
      default -> "UNKNOWN";
    });

    List<String> menuNames = new ArrayList<>();
    if (!menuTitle.isBlank()) menuNames.add(menuTitle);
    if (!menuContent.isBlank()) menuNames.add(menuContent);
    if (!extraInfo.isBlank()) menuNames.add(extraInfo);

    jsonMap.put("menuNames", menuNames);

    try {
      return mapper.writeValueAsString(jsonMap);
    } catch (JsonProcessingException e) {
      return "{}";
    }
  }
}
