package com.ezkorea.hybrid_app.service.adjustment;

import com.amazonaws.services.kms.model.NotFoundException;
import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.adjustment.AdjustmentRepository;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import com.ezkorea.hybrid_app.web.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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

}
