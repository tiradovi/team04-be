package com.example.crawler.meal.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MenuResponseDto {
    private Integer menuId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String mealType;
    private String menuTitle;
    private String menuContent;
    private String extraInfo;

}
