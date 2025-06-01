package com.example.crawler.meal.component.crawling.menu;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MealMenuFormatter {

  private static final Pattern COMMON_PATTERN = Pattern.compile("[♥★♡☺/\\[\\],]|\\[[^\\]]{0,20}]|\\([^)]{0,20}\\)");
  private static final Pattern EMOJI_PATTERN = Pattern.compile("\\p{So}");
  private static final Pattern UNIT_PATTERN = Pattern.compile("\\b\\d{1,3}/\\d{1,3}개|\\b\\d{1,3}(개|g|인분|잔|그릇)\\b");
  private static final Pattern TITLE_PREFIX_PATTERN = Pattern.compile(
      "^(\\[.{0,20}?DAY.{0,10}?]|신메뉴\\S{0,10})\\s*",
      Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
  );
  private static final Pattern AFTER_DESSERT_PATTERN = Pattern.compile("\\s{0,5}후식\\s{0,5}:\\s{0,5}");

  public Integer formatMenuId(LocalDate date, String mealType) {
    int typeCode = switch (mealType) {
      case "breakfast" -> 1;
      case "lunch" -> 2;
      case "dinner" -> 3;
      default -> 0;
    };

    return Integer.parseInt(String.format("%04d%02d%02d%02d",
        date.getYear(),
        date.getMonthValue(),
        date.getDayOfMonth(),
        typeCode
    ));
  }

  public LocalDate formatDate(String rawDay) {
    String[] dateParts = rawDay.split(" ")[0].split("\\.");
    if (dateParts.length < 2) {
      throw new IllegalArgumentException("날짜 형식이 잘못되었습니다: " + rawDay);
    }
    int month = Integer.parseInt(dateParts[0]);
    int day = Integer.parseInt(dateParts[1]);
    return LocalDate.of(LocalDate.now().getYear(), month, day);
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
    content = AFTER_DESSERT_PATTERN.matcher(content).replaceAll(" ");
    content = UNIT_PATTERN.matcher(content).replaceAll("");
    content = EMOJI_PATTERN.matcher(content).replaceAll("");
    content = COMMON_PATTERN.matcher(content).replaceAll("");
    content = content.replace("&", " ");
    content = content.replaceAll("[A-Za-z]", "");
    content = content.replaceAll("\\d", "");
    content = content.replaceAll("\\s+", " ");
    return content.trim();
  }

  public String formatMealTitle(String rawTitle) {
    String title = rawTitle;
    title = EMOJI_PATTERN.matcher(title).replaceAll("");
    title = COMMON_PATTERN.matcher(title).replaceAll("");
    title = title.replaceAll("[\\[\\]]", "");
    title = title.replaceAll("(?i)day", "");
    title = title.replaceAll("신메뉴.*", "신메뉴");
    title = title.replaceAll("일품.*", "일품메뉴");
    title = title.replaceAll("분식.*", "분식메뉴");
    title = title.replaceAll("블랙.*", "이벤트메뉴");
    title = title.replaceAll("계절.*", "계절메뉴");
    title = title.replaceAll("누들.*", "누들");
    return title.trim();
  }

  public String extractTitleFromContent(String rawContent) {
    Matcher matcher = TITLE_PREFIX_PATTERN.matcher(rawContent);
    return matcher.find() ? matcher.group(1).trim() : "";
  }
}
