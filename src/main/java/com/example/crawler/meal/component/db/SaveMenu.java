package com.example.crawler.meal.component.db;

import com.example.crawler.meal.entity.Menu;
import com.example.crawler.meal.repository.MenuRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveMenu {

  private final MenuRepository menuRepository;

  public List<Menu> saveMenus(List<Menu> menus) {
    Set<Integer> existingIds = menuRepository.findAllById(
        menus.stream().map(Menu::getMenuId).toList()
    ).stream().map(Menu::getMenuId).collect(Collectors.toSet());

    List<Menu> newMenus = menus.stream()
        .filter(menu -> !existingIds.contains(menu.getMenuId()))
        .toList();

    if (newMenus.isEmpty()) {
      return List.of();
    }

    return menuRepository.saveAll(newMenus);
  }
}
