package com.example.webscraper.meal.repository;

import com.example.webscraper.meal.entity.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealItemRepository extends JpaRepository<MealItem, Long> {

}
