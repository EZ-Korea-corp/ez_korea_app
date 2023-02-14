package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerRestController {

    private final ManagerService managerService;

    @PutMapping("/approval")
    public ResponseEntity<Object> updateMemberApproval(@RequestBody Map<String, Object> datum) {
        String idStr = (String) datum.get("id");
        managerService.updateMemberStatus(Long.valueOf(idStr), MemberStatus.FULL_TIME);
        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/team")
    public ResponseEntity<Object> createNewTeam(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());
        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");
        String[] arr = teamEmployee.split(",");
        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/team")
    public ResponseEntity<Object> modifyTeam(@RequestBody Map<String, Object> datum) {

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/member")
    public ResponseEntity<Object> updateMemberRole(@RequestBody Map<String, Object> datum) {
        String username = (String) datum.get("username");
        String memberRole = (String) datum.get("memberRole");
        String memberStatus = (String) datum.get("memberStatus");
        managerService.updateMemberRole(username, Role.valueOf(memberRole), MemberStatus.valueOf(memberStatus));
        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }
}
