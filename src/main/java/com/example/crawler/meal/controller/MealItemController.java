package com.example.crawler.meal.controller;

import com.example.crawler.meal.entity.MealItem;
import com.example.crawler.meal.service.MealItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team4/meal")
@RequiredArgsConstructor
public class MealItemController {
    private final MealItemService mealItemService;

    @GetMapping("/items")
    public ResponseEntity<List<MealItem>> getMealItems() {
        return ResponseEntity.ok(mealItemService.getMealItems());
    }
}
