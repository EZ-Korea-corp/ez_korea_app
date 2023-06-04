package com.ezkorea.hybrid_app.domain.adjustment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    Adjustment findAdjustmentById(Long id);

    Optional<Adjustment> findByTeamNoAndAdjDate(Long teamId, LocalDate date);

    boolean existsByTeamNoAndAdjDate(Long teamId, LocalDate date);
}
