package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    void deleteByGasStation(GasStation gasStation);
}