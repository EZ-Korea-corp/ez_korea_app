package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.adjustment.AdjustmentService;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdjustmentRestController {
    private final AdjustmentService adjustmentService;

    @PostMapping("/adj/save")
    public void saveAdjustment(@RequestBody AdjustmentDto dto,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        Member currentMember = securityUser.getMember();
        if (dto.getAdjDate() == null) {
            dto.setAdjDate(LocalDate.now());
        }
        log.info("dto={}", dto);
    }
}
