package com.example.crawler.meal.component.crawling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Slf4j
@Component
public class MealCategory {

  private static final String API_URL =
      "http://k8s-msaservices-7d023f0bb9-676035063.ap-northeast-2.elb.amazonaws.com/api/team3/llmchatbot/categorization/";

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();


  public String classify(String foodName) {
    String cleanedFoodName = foodName == null ? "" : foodName.trim();

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      String jsonBody = objectMapper.writeValueAsString(Map.of("food_name", cleanedFoodName));
      HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

      ResponseEntity<CategoryResponse> response = restTemplate.exchange(
          API_URL,
          HttpMethod.POST,
          request,
          CategoryResponse.class
      );

      if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
        String category = response.getBody().category();
        if (category != null && !category.isBlank()) {
          return category;
        } else {
          log.warn("API 응답은 200이나 category가 비어 있음 - {}", cleanedFoodName);
        }
      } else {
        log.warn("분류 API 응답 실패 - 상태 코드: {}, 음식: {}", response.getStatusCode(), cleanedFoodName);
      }

    } catch (RestClientException e) {
      log.error("RestTemplate 예외 발생 - 음식: {}, 메시지: {}", cleanedFoodName, e.getMessage(), e);
    } catch (Exception e) {
      log.error("알 수 없는 예외 - 음식: {}, 메시지: {}", cleanedFoodName, e.getMessage(), e);
    }

    return "UNKNOWN";
  }

  public record CategoryResponse(String category) {

  }
}