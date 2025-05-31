package com.example.crawler.meal.repository;

import com.example.crawler.meal.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
  @Query("SELECT DISTINCT m FROM Menu m LEFT JOIN FETCH m.mealMenus WHERE m IN :menus")
  List<Menu> findAllWithMealMenus(@Param("menus") List<Menu> menus);
}
