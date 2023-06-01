package com.ezkorea.hybrid_app.domain.adjustment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
    Adjustment findAdjustmentById(Long id);
}
