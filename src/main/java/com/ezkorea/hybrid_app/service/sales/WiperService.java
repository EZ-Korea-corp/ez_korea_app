package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WiperService {

    private final WiperRepository wiperRepository;

    Wiper findWiperBySizeAndSort(String size, String sort) {
        return wiperRepository.findByWiperSizeAndWiperSort(size, sort);
    }
}
