package com.ezkorea.hybrid_app.domain.adjustment;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdjustmentRepository extends JpaRepository<Adjustment, Long> {
;
    Adjustment findAdjustmentBy(Long id);
}
