package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.adjustment.AdjustmentRepository;
import com.ezkorea.hybrid_app.domain.myBatis.AdjustmentMbRepository;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;

    private final AdjustmentMbRepository adjustmentMbRepository;

    public Adjustment findAdjustmentByTeamNoAndAdjDate(Long id, LocalDate adjDate) {
        return adjustmentRepository.findAdjustmentByTeamNoAndAdjDate(id, adjDate);
    }

    public void adjustmentMbRepository(Long teamId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teamId", teamId);

        adjustmentMbRepository.updateAdjustment(paramMap);
    }


}
