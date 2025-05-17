package com.example.crawler.meal.service;

import com.example.crawler.meal.component.crawling.MealMenuExtractor;
import com.example.crawler.meal.component.crawling.MealMenuHtmlFetcher;
import com.example.crawler.meal.component.crawling.MealMenuParser;
import com.example.crawler.meal.component.crawling.MealMenuUrlGenerator;
import com.example.crawler.meal.entity.Menu;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MealMenuCrawlerService {

  private final MealMenuHtmlFetcher mealMenuHtmlFetcher;
  private final MealMenuExtractor mealMenuExtractor;
  private final MealMenuParser mealMenuParser;
  private final MealMenuUrlGenerator mealMenuUrlGenerator;


  public List<Menu> mealMenuCrawler(boolean onlyThisWeek) {
    List<Menu> totalMenuList = new ArrayList<>();
    List<String> targetUrls = mealMenuUrlGenerator.generateUrls(onlyThisWeek);

    for (String url : targetUrls) {
      try {
        log.info("식단 크롤링 시작 - URL: {}", url);
        Document document = mealMenuHtmlFetcher.fetch(url);
        Element table = mealMenuExtractor.extractWeeklyMenu(document);
        List<Menu> weeklyMenus = mealMenuParser.parse(table);
        totalMenuList.addAll(weeklyMenus);
        log.info("식단 크롤링 성공 - {}건 수집됨", weeklyMenus.size());
      } catch (IOException e) {
        log.error("식단 크롤링 실패 - URL: {}", url, e);
      }
    }
    return totalMenuList;
  }
}