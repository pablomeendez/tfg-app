package com.tfg.tfg_app.model.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DiaryEntryDao extends JpaRepository<DiaryEntry, Long> {

    List<DiaryEntry> findByUserId(Long userId);

    @Query(value = """
    SELECT * FROM DiaryEntry de
    WHERE de.moodId = (
        SELECT moodId
        FROM DiaryEntry
        WHERE userId = ?1 AND date >= ?2 AND date <= ?3
        GROUP BY moodId
        ORDER BY COUNT(*) DESC
        LIMIT 1
    )
    AND de.userId = ?1
    ORDER BY de.date DESC
    LIMIT 1
    """, nativeQuery = true)
    DiaryEntry findMostFrequentMood(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<DiaryEntry> findByUserIdAndDateBetween(Long userId, LocalDateTime startDate, LocalDateTime endDate);
}