package com.example.crawler.meal.controller;

import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/team4/meal")
@RequiredArgsConstructor
public class MealMenuController {

    private final MenuService menuService;

    @GetMapping("/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenuItems(
        @RequestParam(defaultValue = "false") boolean onlyThisWeek) {
        return ResponseEntity.ok(menuService.getMenuItems(onlyThisWeek));
    }
}