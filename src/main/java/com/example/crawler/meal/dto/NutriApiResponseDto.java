package com.example.crawler.meal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import java.util.List;

@Getter
@Data
@ToString
public class NutriApiResponseDto {

    @JsonProperty("response")
    private Response response;

    @Data
    @ToString
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    @ToString
    public static class Header {
        private String resultCode;
        private String resultMsg;
        private String type;
    }

    @Data
    @ToString
    public static class Body {
        @JsonProperty("items")
        private List<Item> items;
        private int numOfRows;
        private int pageNo;
        private int totalCount;
    }

    @Data
    @ToString
    public static class Item {
        @JsonProperty("foodNm")
        private String foodName;

        @JsonProperty("enerc")
        private String calories;

        @JsonProperty("prot")
        private String protein;

        @JsonProperty("fatce")
        private String fat;

        @JsonProperty("chocdf")
        private String carb;

        @JsonProperty("nutConSrtrQua")
        private String foodWeight;

    }
}