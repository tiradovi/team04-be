package com.example.crawler.meal.component;

import com.example.crawler.meal.dto.MealSummaryDto;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.MealMenu;
import com.example.crawler.meal.entity.Menu;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MenuMapper {

  public MenuResponseDto toDto(Menu menu) {
    List<MealSummaryDto> mealSummaries = menu.getMealMenus().stream()
        .map(MealMenu::getMeal)
        .filter(meal -> meal != null)
        .map(meal -> MealSummaryDto.builder()
            .mealName(meal.getMealName())
            .mealCategory(meal.getMealCategory())
            .build())
        .toList();

    return MenuResponseDto.builder()
        .menuId(menu.getMenuId())
        .date(menu.getDate())
        .mealType(menu.getMealType())
        .menuTitle(menu.getMenuTitle())
        .menuContent(menu.getMenuContent())
        .extraInfo(menu.getExtraInfo())
        .meals(mealSummaries)
        .build();
  }
}
