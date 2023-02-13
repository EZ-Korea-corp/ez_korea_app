package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerRestController {

    private final ManagerService managerService;

    @PutMapping("/approval")
    public HttpStatus updateMemberApproval(@RequestBody Map<String, Object> datum) {
        String idStr = (String) datum.get("id");
        managerService.updateMemberStatus(Long.valueOf(idStr), MemberStatus.FULL_TIME);
        return HttpStatus.OK;
    }

    @PutMapping("/member")
    public ResponseEntity<Object> updateMemberRole(@RequestBody Map<String, Object> datum) {
        String username = (String) datum.get("username");
        String memberRole = (String) datum.get("memberRole");
        String memberStatus = (String) datum.get("memberStatus");
        log.info("username={}", username);
        log.info("memberRole={}", memberRole);
        log.info("memberStatus={}", memberStatus);
        managerService.updateMemberRole(username, Role.valueOf(memberRole), MemberStatus.valueOf(memberStatus));
        return new ResponseEntity<>("반영되었습니다.", HttpStatus.OK);
    }
}
