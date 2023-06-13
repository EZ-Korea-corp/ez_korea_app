package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.adjustment.AdjustmentService;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdjustmentRestController {
    private final AdjustmentService adjustmentService;

    @PostMapping("/adj/save")
    public ResponseData.ApiResult<?> saveAdjustment(@RequestBody AdjustmentDto dto,
                                                    @AuthenticationPrincipal SecurityUser securityUser) {
        Member currentMember = securityUser.getMember();
        if ((currentMember.getTeam() == null) || (currentMember.getTeam().getId() != dto.getTeamNo())) {
            return ResponseData.error("다른 팀의 정산 내용은 접근할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        if (dto.getAdjDate() == null) {
            dto.setAdjDate(LocalDate.now());
        }
        adjustmentService.saveAdjustment(dto);
        return ResponseData.success(null, "저장되었습니다.");
    }

    @PutMapping("/adj/update")
    public ResponseData.ApiResult<?> updateAdjustment(@RequestBody AdjustmentDto dto,
                                                    @AuthenticationPrincipal SecurityUser securityUser) {

        Member currentMember = securityUser.getMember();
        if ((currentMember.getTeam() == null) || (currentMember.getTeam().getId() != dto.getTeamNo())) {
            return ResponseData.error("다른 팀의 정산 내용은 접근할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }

        adjustmentService.updateAdjustment(dto);

        return ResponseData.success(null, "수정되었습니다.");
    }
}
