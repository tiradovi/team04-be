package com.example.crawler.meal.component.crawling;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MealMenuFormatter {

  private static final Pattern COMMON_PATTERN = Pattern.compile("[♥★♡☺/\\[\\],]|\\[[^\\]]{0,20}]|\\([^)]{0,20}\\)");
  private static final Pattern EMOJI_PATTERN = Pattern.compile("[\\u2600-\\u26FF]|[\\uD83C\\uDF00-\\uD83D\\uDEFF]");
  private static final Pattern UNIT_PATTERN = Pattern.compile("\\b\\d{1,3}/\\d{1,3}개|\\b\\d{1,3}(개|g|인분|잔|그릇)\\b");
  private static final Pattern TITLE_PREFIX_PATTERN = Pattern.compile(
      "^(\\[.{0,20}?DAY.{0,10}?]|신메뉴\\S{0,10})\\s*",
      Pattern.CASE_INSENSITIVE
  );
  private static final Pattern AFTER_DESSERT_PATTERN = Pattern.compile("\\s{0,5}후식\\s{0,5}:\\s{0,5}");

  public Integer formatMenuId(LocalDate date, String mealType) {
    int typeCode = switch (mealType) {
      case "breakfast" -> 1;
      case "lunch" -> 2;
      case "dinner" -> 3;
      default -> 0;
    };

    String idStr = String.format("%04d%02d%02d%02d",
        date.getYear(),
        date.getMonthValue(),
        date.getDayOfMonth(),
        typeCode
    );
    return Integer.parseInt(idStr);
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
    String content = TITLE_PREFIX_PATTERN.matcher(rawContent.trim()).replaceFirst("");

    return AFTER_DESSERT_PATTERN.matcher(content).replaceAll(" ")
        .replaceAll(UNIT_PATTERN.pattern(), "")
        .replaceAll(EMOJI_PATTERN.pattern(), "")
        .replaceAll(COMMON_PATTERN.pattern(), "")
        .replaceAll("&", " ") // 직접적으로 "&" 제거
        .replaceAll("[A-Za-z]", "")
        .replaceAll("\\d", "")
        .replaceAll("\\s+", " ")
        .trim();
  }

  public String formatMealTitle(String rawTitle) {
    return rawTitle
        .replaceAll(EMOJI_PATTERN.pattern(), "")
        .replaceAll(COMMON_PATTERN.pattern(), "")
        .replaceAll("[\\[\\]]", "")
        .replaceAll("(?i)day", "")
        .replaceAll("신메뉴.*", "신메뉴")
        .replaceAll("일품.*", "일품메뉴")
        .replaceAll("분식.*", "분식메뉴")
        .replaceAll("블랙.*", "이벤트메뉴")
        .replaceAll("계절.*", "계절메뉴")
        .replaceAll("누들.*", "누들")
        .trim();
  }

  public String extractTitleFromContent(String rawContent) {
    Matcher matcher = TITLE_PREFIX_PATTERN.matcher(rawContent);
    if (matcher.find()) {
      return matcher.group(1).trim();
    }
    return "";
  }
}
