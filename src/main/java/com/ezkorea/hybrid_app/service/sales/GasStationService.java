package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.web.exception.GasStationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GasStationService {
    private final GasStationRepository gsRepository;

    public GasStation findByStationName(String name) {
        return gsRepository.findByStationName(name)
                .orElseThrow( () -> new GasStationNotFoundException(name + "를 찾을 수 없습니다."));
    }
}
