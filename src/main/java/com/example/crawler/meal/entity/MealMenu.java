package com.example.crawler.meal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"menu_id", "meal_id"}))
public class MealMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    @JsonBackReference("menu-mealMenu")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "meal_id")
    @JsonBackReference("meal-mealMenu")
    private Meal meal;

    public MealMenu(Menu menu, Meal meal) {
        this.menu = menu;
        this.meal = meal;
    }

    public static MealMenu of(Menu menu, Meal meal) {
        return MealMenu.builder()
            .menu(menu)
            .meal(meal)
            .build();
    }
}