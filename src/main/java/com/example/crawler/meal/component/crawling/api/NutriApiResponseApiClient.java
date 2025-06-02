package com.example.crawler.meal.component.crawling.api;

import com.example.crawler.meal.dto.NutriApiResponseDto;
import com.example.crawler.meal.dto.NutriApiResponseDto.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class NutriApiResponseApiClient {

  private static final String PRIMARY_API_URL = "http://api.data.go.kr/openapi/tn_pubr_public_nutri_food_info_api";
  private static final String FALLBACK_API_URL = "http://api.data.go.kr/openapi/tn_pubr_public_nutri_process_info_api";

  @Value("${api.nutri.service-key}")
  private String serviceKey;

  public Item fetchNutritionInfo(String foodName) {
    try {
      log.info("영양 정보 조회 시작 (기본 API): {}", foodName);
      Item item = fetchFromApi(PRIMARY_API_URL, foodName);
      if (item != null) return item;

      log.warn("기본 API에서 결과 없음, 백업 API 시도 중...");
      item = fetchFromApi(FALLBACK_API_URL, foodName);
      if (item != null) return item;

      log.warn("⚠️ 백업 API에서도 결과 없음: {}", foodName);
      return null;

    } catch (Exception e) {
      log.error("❌ API 호출 중 예외 발생: {}", foodName, e);
      return null;
    }
  }

  private Item fetchFromApi(String apiUrl, String foodName) {
    try {
      String encodedFoodName = URLEncoder.encode(foodName, StandardCharsets.UTF_8);
      String fullUrl = apiUrl +
          "?serviceKey=" + serviceKey +
          "&pageNo=1" +
          "&numOfRows=10" +
          "&type=json" +
          "&foodLv4Nm=" + encodedFoodName;

      log.info("Request URL: {}", fullUrl);

      NutriApiResponseDto response = WebClient.create()
          .get()
          .uri(URI.create(fullUrl))
          .retrieve()
          .bodyToMono(NutriApiResponseDto.class)
          .block();

      log.debug("API 응답: {}", response);

      if (response == null || response.getResponse() == null ||
          response.getResponse().getBody() == null ||
          response.getResponse().getBody().getItems() == null ||
          response.getResponse().getBody().getItems().isEmpty()) {
        return null;
      }

      Item item = response.getResponse().getBody().getItems().get(0);
      log.info("✅ 영양 정보 - {}: 칼로리: {}, 단백질: {}, 지방: {}, 탄수화물: {}",
          item.getFoodName(), item.getCalories(), item.getProtein(), item.getFat(), item.getCarb());
      return item;

    } catch (WebClientResponseException e) {
      log.error("❌ API 응답 오류 ({}): 상태코드: {}, 응답: {}", apiUrl, e.getStatusCode(), e.getResponseBodyAsString(), e);
      return null;
    } catch (Exception e) {
      log.error("❌ API 호출 오류 ({}): {}", apiUrl, foodName, e);
      return null;
    }
  }
}
