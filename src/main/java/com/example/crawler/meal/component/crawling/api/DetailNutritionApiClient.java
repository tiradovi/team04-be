package com.example.crawler.meal.component.crawling.api;

import com.example.crawler.meal.dto.DetailNutritionRequestDto;
import com.example.crawler.meal.dto.DetailNutritionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class DetailNutritionApiClient {

  private static final String DETAIL_API_URL = "http://k8s-msaservices-7d023f0bb9-676035063.ap-northeast-2.elb.amazonaws.com/api/team3/llmchatbot/detail_categorization/";

  public DetailNutritionResponseDto fetchDetailInfo(String foodName) {
    try {
      DetailNutritionRequestDto requestDto = new DetailNutritionRequestDto(foodName);

      log.info("상세 영양 API 요청 시작: 음식명 = {}", foodName);

      DetailNutritionResponseDto response = WebClient.create()
          .post()
          .uri(DETAIL_API_URL)
          .bodyValue(requestDto)
          .retrieve()
          .bodyToMono(DetailNutritionResponseDto.class)
          .block();

      if (response == null) {
        log.warn("상세 영양 API에서 빈 응답이 반환되었습니다: 음식명 = {}", foodName);
      }

      return response;

    } catch (Exception e) {
      log.error("상세 영양 API 호출 중 예외 발생: 음식명 = {}", foodName, e);
      return null;
    }
  }
}
