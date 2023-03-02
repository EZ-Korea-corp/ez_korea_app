package com.ezkorea.hybrid_app.service.expenses;

import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCost;
import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class FuelCostService {

    private final FuelCostRepository fcRepository;

    public FuelCost findFuelCostByLocalDate(LocalDate localDate) {
        return fcRepository.findByBaseDate(localDate);
    }

}
