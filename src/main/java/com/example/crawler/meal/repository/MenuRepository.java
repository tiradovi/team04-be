package com.example.crawler.meal.repository;

import com.example.crawler.meal.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
