package com.example.crawler.meal.controller;

import com.example.crawler.meal.dto.MealResponseDto;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.service.MealService;
import com.example.crawler.meal.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team4/meal")
@RequiredArgsConstructor
public class MealMenuController {

  private final MenuService menuService;
  private final MealService mealService;

  @GetMapping("/items")
  public ResponseEntity<List<MenuResponseDto>> getMenuItems() {
    return ResponseEntity.ok(menuService.getMenuItems());
  }

  @GetMapping("/nutrition")
  public ResponseEntity<MealResponseDto> getMealDetail(@RequestParam String foodName) {
    return ResponseEntity.ok(mealService.getNutritionByMealName(foodName));
  }
}
