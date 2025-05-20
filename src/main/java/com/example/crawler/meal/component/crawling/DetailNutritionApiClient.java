package com.example.crawler.meal.component.crawling;

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

      log.info("Requesting detail nutrition API with foodName: {}", foodName);

      DetailNutritionResponseDto response = WebClient.create()
          .post()
          .uri(DETAIL_API_URL)
          .bodyValue(requestDto)
          .retrieve()
          .bodyToMono(DetailNutritionResponseDto.class)
          .block();

      if (response == null) {
        log.warn("Empty response from detail nutrition API for food: {}", foodName);
      }

      return response;

    } catch (Exception e) {
      log.error("Exception calling detail nutrition API for food: {}", foodName, e);
      return null;
    }
  }
}
