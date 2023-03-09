package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.etc.EtcService;
import com.ezkorea.hybrid_app.service.stat.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/etc")
@RequiredArgsConstructor
@Slf4j
public class EtcRestController {
    private final EtcService etcService;

    @PostMapping("/msgToCeo/save")
    public HttpStatus saveMsgToCeo(@RequestBody Map<String, String> data,
                                   @AuthenticationPrincipal SecurityUser securityUser) throws Exception {
        etcService.saveMsgToCeo(data, securityUser.getMember());

        return HttpStatus.OK;
    }

    @DeleteMapping("/msgToCeo/delete")
    public HttpStatus deleteMsgToCeo(@RequestBody Map<String, Long> data) throws Exception {
        etcService.deleteMsgToCeo(data.get("id"));

        return HttpStatus.OK;
    }

}
