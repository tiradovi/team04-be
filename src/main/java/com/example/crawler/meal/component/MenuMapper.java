package com.example.crawler.meal.component;

import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.Menu;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

  public MenuResponseDto toDto(Menu menu) {
    return MenuResponseDto.builder()
        .menuId(menu.getMenuId())
        .date(menu.getDate())
        .mealType(menu.getMealType())
        .menuTitle(menu.getMenuTitle())
        .menuContent(menu.getMenuContent())
        .extraInfo(menu.getExtraInfo())
        .build();
  }
}
