package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.StockRepository;
import com.ezkorea.hybrid_app.web.exception.GasStationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasStationService {
    private final GasStationRepository gsRepository;
    private final StockRepository stockRepository;
    private final SaleMbRepository saleMbRepository;

    public GasStation findByStationName(String name) {
        return gsRepository.findByStationName(name)
                .orElseThrow( () -> new GasStationNotFoundException(name + "를 찾을 수 없습니다."));
    }

    public List<GasStation> findAllGasStation() {
        return gsRepository.findAll();
    }

    public GasStation findStationById(Long id) {
        Optional<GasStation> station = gsRepository.findById(id);
        return station.get();
    }
}
