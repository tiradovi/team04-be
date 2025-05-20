package com.example.crawler.meal.component;

import com.example.crawler.meal.dto.MealMenuResponseDto;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.MealMenu;
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
            .mealMenus(menu.getMealMenus().stream()
                .map(this::toMealMenuDto)
                .toList())
            .build();
    }

    private MealMenuResponseDto toMealMenuDto(MealMenu mealMenu) {
        var meal = mealMenu.getMeal();

        return MealMenuResponseDto.builder()
            .mealMenuId(mealMenu.getId())
            .menuItem(meal.getMealName())
            .mealCategory(meal.getMealCategory())
            .allergy(meal.getAllergy())
            .calorie_kcal(meal.getCalorie_kcal())
            .carb_g(meal.getCarb_g())
            .protein_g(meal.getProtein_g())
            .fat_g(meal.getFat_g())
            .foodWeight(meal.getFoodWeight())
            .build();
    }
}
