package com.example.crawler.meal.component;

import com.example.crawler.meal.entity.MealItem;
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
public class MealTableParser {

  private final MealItemFormatter formatter;

  public List<MealItem> parse(Element tableElement) {
    List<MealItem> mealItems = new ArrayList<>();
    if (tableElement == null) {
      throw new IllegalStateException("식단 데이터를 수집하지 못했습니다.");
    }

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
              buildMealItem(currentDay, cells.get(1), cells.get(2), cells.get(3), cells.get(4)));
        }
      } else if (currentDay != null && cells.size() >= 4) {
        mealItems.add(
            buildMealItem(currentDay, cells.get(0), cells.get(1), cells.get(2), cells.get(3)));
      }
    }
    return mealItems;
  }

  private MealItem buildMealItem(String rawDay, Element rawMealType, Element rawTitle,
      Element rawContent, Element rawExtra) {
    String mealType = formatter.formatMealType(rawMealType.text());
    String title = rawTitle.text().trim();
    String content = formatter.formatMenuContent(rawContent.text());
    String extra = rawExtra.text().trim();
    LocalDate date = formatter.formatDate(rawDay);
    int id = Integer.parseInt(formatter.formatId(mealType, date));

    return MealItem.of(id, date, mealType, title, content, extra);
  }
}
