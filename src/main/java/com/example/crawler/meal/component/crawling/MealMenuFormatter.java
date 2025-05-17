package com.example.crawler.meal.component.crawling;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MealMenuFormatter {

  private static final String COMMON_REGEX = "[♥★♡☺&/()\\[\\],]|\\[.*?]|\\(.*?\\)";
  private static final String EMOJI_REGEX = "[\\u2600-\\u26FF]|[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]";
  private static final String UNIT_REGEX = "\\d+/\\d+개|\\d+개|\\d+g|\\d+인분|\\d+잔|\\d+그릇";

  private static final Pattern TITLE_PREFIX_PATTERN = Pattern.compile(
      "^(\\[.*?\\s*DAY.*?]|신메뉴\\S{0,10})\\s*",
      Pattern.CASE_INSENSITIVE
  );

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

    return content
        .replaceAll(UNIT_REGEX, "")
        .replaceAll(EMOJI_REGEX, "")
        .replaceAll(COMMON_REGEX, "")
        .trim();
  }

  public String formatMealTitle(String rawTitle) {
    return rawTitle
        .replaceAll(EMOJI_REGEX, "")
        .replaceAll(COMMON_REGEX, "")
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
