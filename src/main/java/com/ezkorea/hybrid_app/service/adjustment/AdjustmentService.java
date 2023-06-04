package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.adjustment.AdjustmentRepository;
import com.ezkorea.hybrid_app.domain.myBatis.AdjustmentMbRepository;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import com.ezkorea.hybrid_app.web.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentService {

    private final AdjustmentRepository adjustmentRepository;
    private final AdjustmentMbRepository adjustmentMbRepository;

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

    @Transactional
    public void updateAdjustment(AdjustmentDto dto) {
        Adjustment currentAdj = findByTeamNoAndAdjDate(dto.getTeamNo(), dto.getAdjDate());
        currentAdj.updateAdj(dto);
    }

    public Adjustment findByTeamNoAndAdjDate(Long teamNo, LocalDate date) {
        return adjustmentRepository.findByTeamNoAndAdjDate(teamNo, date)
                .orElseThrow(() -> new TeamNotFoundException("팀을 찾을 수 없습니다."));
    }

    public boolean existsByTeamNoAndAdjDate(Long teamNo, LocalDate date) {
        return adjustmentRepository.existsByTeamNoAndAdjDate(teamNo, date);
    }

    public void adjustmentMbRepository(Long teamId) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("teamId", teamId);

        adjustmentMbRepository.updateAdjustment(paramMap);
    }

}
