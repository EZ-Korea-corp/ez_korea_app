package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.adjustment.AdjustmentRepository;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;

    public void saveAdjustment(AdjustmentDto dto) {
        adjustmentRepository.save(Adjustment.builder()
                .teamNo(dto.getTeamNo())
                .divisionNo(dto.getDivisionNo())
                .cashAdj(dto.getCashAdj())
                .cardAdj(dto.getCardAdj())
                .teamAvg(dto.getTeamAvg())
                .totalAdj(dto.getTotalAdj())
                .accountAdj(dto.getAccountAdj())
                .contentAdj(dto.getContentAdj())
                .adjDate(dto.getAdjDate())
                .build());
    }

}
