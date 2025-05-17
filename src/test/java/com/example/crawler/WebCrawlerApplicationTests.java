package com.mjumeal;

import com.example.crawler.WebCrawlerApplication;
import com.example.crawler.meal.component.MenuMapper;
import com.example.crawler.meal.dto.MealMenuResponseDto;
import com.example.crawler.meal.dto.MenuResponseDto;
import com.example.crawler.meal.entity.MealMenu;
import com.example.crawler.meal.entity.Meal;
import com.example.crawler.meal.entity.Menu;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = WebCrawlerApplication.class)
class MenuMapperTest {

	private final MenuMapper menuMapper = new MenuMapper();
	@Test
	void 엔티티를_DTO로_매핑한다() {
		// GIVEN
		Meal meal = Meal.builder()
				.mealName("김치찌개")
				.build();

		MealMenu mealMenu = MealMenu.builder()
				.id(1)
				.meal(meal)
				.build();

		Menu menu = Menu.builder()
				.menuId(100)
				.date(LocalDate.of(2025, 5, 2))
				.mealType("점심")
				.menuTitle("오늘의 메뉴")
				.menuContent("국/반찬/후식")
				.extraInfo("특이사항 없음")
				.mealMenus(List.of(mealMenu))
				.build();

		// WHEN
		MenuResponseDto dto = menuMapper.toDto(menu);

		// THEN
		assertThat(dto.getMenuId()).isEqualTo(100L);
		assertThat(dto.getDate()).isEqualTo(LocalDate.of(2025, 5, 2));
		assertThat(dto.getMealType()).isEqualTo("점심");
		assertThat(dto.getMenuTitle()).isEqualTo("오늘의 메뉴");
		assertThat(dto.getMenuContent()).isEqualTo("국/반찬/후식");
		assertThat(dto.getExtraInfo()).isEqualTo("특이사항 없음");
		assertThat(dto.getMealMenus()).hasSize(1);

		MealMenuResponseDto mealMenuDto = dto.getMealMenus().get(0);
		assertThat(mealMenuDto.getMealMenuId()).isEqualTo(1L);
		assertThat(mealMenuDto.getMenuItem()).isEqualTo("김치찌개");
	}
}
