package com.example.crawler.event.repository;

import com.example.crawler.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
  Optional<Event> findByTitleAndDate(String title, LocalDate date);
}
