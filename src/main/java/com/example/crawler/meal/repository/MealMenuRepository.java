package com.example.crawler.meal.repository;

import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.MealMenu;
import com.example.crawler.meal.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealMenuRepository extends JpaRepository<MealMenu, Integer> {
  Optional<MealMenu> findByMenuAndMeal(Menu menu, Meal meal);
}