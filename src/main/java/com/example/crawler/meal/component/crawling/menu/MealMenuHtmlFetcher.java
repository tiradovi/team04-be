package com.example.crawler.meal.component.crawling.menu;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MealMenuHtmlFetcher {

  private static final String USER_AGENT = "Mozilla/5.0";

  public Document fetch(String url) throws IOException {
    return Jsoup.connect(url)
        .userAgent(USER_AGENT)
        .timeout(15000)
        .get();
  }
}
