package com.ezkorea.hybrid_app.domain.expenses.fuel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface FuelCostRepository extends JpaRepository<FuelCost, Long> {
    FuelCost findByBaseDate(LocalDate localDate);

}
