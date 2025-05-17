package com.example.crawler.meal.component.crawling;

import com.example.crawler.meal.entity.Menu;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

  public List<Menu> parse(Element tableElement) {
    List<Menu> mealItems = new ArrayList<>();
    Elements rows = tableElement.select("tbody tr");
    String currentDay = null;

    for (Element row : rows) {
      Elements cells = row.select("th, td");
      if (cells.isEmpty()) {
        continue;
      }

      String firstCell = cells.get(0).text().trim();
      if (firstCell.matches("\\d{2}\\.\\d{2}.*\\([월화수목금]\\).*")) {
        currentDay = firstCell;
        if (cells.size() >= 5) {
          mealItems.add(
              buildMenuItem(currentDay, cells.get(1), cells.get(2), cells.get(3), cells.get(4)));
        }
      } else if (currentDay != null && cells.size() >= 4) {
        mealItems.add(
            buildMenuItem(currentDay, cells.get(0), cells.get(1), cells.get(2), cells.get(3)));
      }
    }
    return mealItems;
  }

  private Menu buildMenuItem(String rawDay, Element rawMealType, Element rawTitle,
      Element rawContent, Element rawExtra) {
    String mealType = formatter.formatMealType(rawMealType.text());
    String extractedTitle = formatter.extractTitleFromContent(rawContent.text());
    String title =
        !extractedTitle.isBlank() ? extractedTitle : formatter.formatMealTitle(rawTitle.text());
    String content = formatter.formatMenuContent(rawContent.text());
    String extra = rawExtra.text().trim();
    LocalDate date = formatter.formatDate(rawDay);
    Integer generatedId = formatter.formatMenuId(date, mealType);

    return Menu.of(generatedId, date, mealType, title, content, extra);
  }
}
