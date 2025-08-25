package com.example.crawler.meal.component.crawling.menu;

import com.example.crawler.meal.entity.Menu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MealMenuParser {

  private final MealMenuFormatter formatter;
  private static final Pattern DATE_PATTERN = Pattern.compile("\\d{2}\\.\\d{2}.*?\\([월화수목금]\\)", Pattern.DOTALL);

  public List<Menu> parse(Element tableElement) {
    List<Menu> mealItems = new ArrayList<>();
    Elements rows = tableElement.select("tbody tr");
    String currentDay = null;


    for (int i = 0; i < rows.size(); i++) {
      Element row = rows.get(i);
      Elements cells = row.select("th, td");
      if (cells.isEmpty()) {
        continue;
      }

      log.debug("행 {}: 셀 개수={}, 첫번째 셀={}", i, cells.size(),
          cells.get(0).text().trim().substring(0, Math.min(20, cells.get(0).text().trim().length())));

      String firstCellHtml = cells.get(0).html().trim();
      String firstCellText = cells.get(0).text().trim();


      boolean isDateRow = DATE_PATTERN.matcher(firstCellHtml).find() ||
          DATE_PATTERN.matcher(firstCellText).find() ||
          firstCellText.matches("\\d{2}\\.\\d{2}.*");

      if (isDateRow && cells.get(0).tagName().equals("th")) {
        currentDay = firstCellText;

        if (cells.size() >= 5) {
          Menu menu = buildMenuItem(currentDay, cells.get(1), cells.get(2), cells.get(3), cells.get(4));
          if (menu != null) {
            mealItems.add(menu);
          }
        }
      } else if (currentDay != null) {

        if (cells.size() >= 4) {

          Element contentCell = cells.get(2);
          if (contentCell.hasAttr("colspan")) {
            log.debug("식단 정보 없음 - 건너뛰기: {}", contentCell.text());
            continue;
          }

          Menu menu = buildMenuItem(currentDay, cells.get(0), cells.get(1), cells.get(2), cells.get(3));
          if (menu != null) {
            mealItems.add(menu);
          }
        }
      }
    }
    return mealItems;
  }

  private Menu buildMenuItem(String rawDay, Element rawMealType, Element rawTitle,
      Element rawContent, Element rawExtra) {

    try {

      if (!formatter.isValidMenuContent(rawContent.text())) {
        log.debug("유효하지 않은 식단 내용으로 건너뛰기: {}", rawContent.text().trim());
        return null;
      }

      String mealType = formatter.formatMealType(rawMealType.text());
      String extractedTitle = formatter.extractTitleFromContent(rawContent.text());
      String titleRaw = !extractedTitle.isBlank()
          ? extractedTitle
          : formatter.formatMealTitle(rawTitle.text());

      String title = formatter.formatMealTitle(titleRaw);
      String content = formatter.formatMenuContent(rawContent.text());


      if (content == null || content.trim().isEmpty()) {

        return null;
      }

      String extra = rawExtra.text().trim();
      LocalDate date = formatter.formatDate(rawDay);
      Integer generatedId = formatter.formatMenuId(date, mealType);

      log.debug("메뉴 아이템 생성: {} - {} - {}", date, mealType, title);
      return Menu.of(generatedId, date, mealType, title, content, extra);

    } catch (Exception e) {
      log.error("메뉴 아이템 생성 중 오류 발생: {}", e.getMessage(), e);
      return null;
    }
  }
}