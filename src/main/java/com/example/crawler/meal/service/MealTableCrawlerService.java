package com.example.crawler.meal.service;

import com.example.crawler.meal.component.MealTableExtractor;
import com.example.crawler.meal.component.MealTableHtmlFetcher;
import com.example.crawler.meal.component.MealTableParser;
import com.example.crawler.meal.entity.MealItem;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealTableCrawlerService {

  private static final String TARGET_URL = "https://www.mju.ac.kr/mjukr/8595/subview.do";

  private final MealTableHtmlFetcher mealtableHtmlFetcher;
  private final MealTableExtractor mealtableExtractor;
  private final MealTableParser mealtableParser;

  public List<MealItem> mealTableCrawler() {
    try {
      Document document = mealtableHtmlFetcher.fetch(TARGET_URL);
      Element table = mealtableExtractor.extractWeeklyMenuTable(document);
      return mealtableParser.parse(table);
    } catch (IOException e) {
      log.error("식단 테이블 크롤링 중 오류 발생", e);
      return Collections.emptyList();
    }
  }
}
