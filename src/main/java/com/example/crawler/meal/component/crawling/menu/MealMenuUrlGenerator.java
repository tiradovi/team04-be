package com.example.crawler.meal.component.crawling.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Slf4j
@Component
public class MealMenuUrlGenerator {

  private static final String BASE_URL = "https://www.mju.ac.kr/mjukr/8595/subview.do";
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

  public List<String> generateUrls(boolean onlyThisWeek) {
    List<String> urls = new ArrayList<>();
    urls.add(BASE_URL);
    if (!onlyThisWeek) {
      LocalDate today = LocalDate.now();
      LocalDate thisMonday = today.minusDays(today.getDayOfWeek().getValue() - 1L);
      for (int i = 0; i <= 5; i++) {
        LocalDate monday = thisMonday.minusWeeks(i);
        urls.add(createUrlWithDate(monday));
      }
    }
    return urls;
  }


  private String createUrlWithDate(LocalDate monday) {
    String formattedDate = monday.format(DATE_FORMATTER);
    String raw = "/diet/mjukr/10/view.do?monday=" + formattedDate + "&week=pre&";

    try {
      String urlEncoded = URLEncoder.encode(raw, StandardCharsets.UTF_8);
      String withPrefix = "fnct1|@@|" + urlEncoded;
      String base64Encoded = Base64.getEncoder()
          .encodeToString(withPrefix.getBytes(StandardCharsets.UTF_8));
      String finalUrl = BASE_URL + "?enc=" + base64Encoded;

      log.info("생성된 URL : {}", finalUrl);
      return finalUrl;
    } catch (Exception e) {
      log.error("URL 생성 중 오류 발생 : {}", e.getMessage());
      return BASE_URL;
    }
  }
}
