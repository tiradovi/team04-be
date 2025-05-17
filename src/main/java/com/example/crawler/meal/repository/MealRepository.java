package com.example.crawler.meal.repository;

import com.example.crawler.meal.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Integer> {
  Optional<Meal> findByMealName(String mealName);
}