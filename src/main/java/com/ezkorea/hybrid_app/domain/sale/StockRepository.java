package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Long> {

    void deleteByGasStationAndDate(GasStation gasStation, LocalDate date);
}