package com.ezkorea.hybrid_app.domain.adjustment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    Adjustment findAdjustmentByTeamNoAndAdjDate(Long id, LocalDate date);
}
