package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.manager.ManagerService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerRestController {

    private final ManagerService managerService;
    private final TeamService tService;
    private final DivisionService dService;
    private final ModelMapper mapper;

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
        String divisionName = (String) datum.get("teamGM");
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");

        Division currentDivision = dService.findDivisionByDivisionName(divisionName);
        TeamDto dto = tService.createTeamDto(currentDivision, teamName, teamLeader, teamEmployee);
        tService.saveNewTeam(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/division")
    public ResponseEntity<Object> createNewDivision(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");

        DivisionDto dto = dService.createDivisionDto(teamName, teamGm);
        if (dto == null) {
            return new ResponseEntity<>(Map.of("message", "무소속 지점은 1개만 만들 수 있습니다."), HttpStatus.BAD_REQUEST);
        }

        dService.saveNewDivision(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/division/{id}")
    public ResponseEntity<Object> updateDivision(@RequestBody Map<String, Object> datum, @PathVariable Long id) {

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");
        dService.updateDivision(id, teamName, teamGm);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/team/{id}")
    public ResponseEntity<Object> modifyTeam(@RequestBody Map<String, Object> datum, @PathVariable Long id) {

        log.info("datum={}", datum.toString());
        String teamName = (String) datum.get("teamName");
        String divisionName = (String) datum.get("teamGM");
        Division currentDivision = dService.findDivisionByDivisionName(divisionName);
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");

        tService.updateTeam(id, currentDivision, teamName, teamLeader, teamEmployee);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/member")
    public ResponseEntity<Object> updateMemberRole(@RequestBody Map<String, Object> datum) {
        String username = (String) datum.get("username");
        String memberRole = (String) datum.get("memberRole");
        String memberStatus = (String) datum.get("memberStatus");

        // 글쓰기 권한
        String memberPostAuth = (String) datum.get("memberPostAuth");

        // 입고 권한
        String memberInputAuth = (String) datum.get("memberInputAuth");

        managerService.updateMemberSubAuth(username, memberPostAuth, memberInputAuth);
        managerService.updateMemberRole(username, Role.valueOf(memberRole), MemberStatus.valueOf(memberStatus));

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/taskList")
    public Map<String, Object> findTaskList(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", managerService.findTaskDateList(data));

        return returnMap;
    }

    @PostMapping("/totalStat")
    public Map<String, Object> findTotalStat(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = managerService.findTotalStat(data);
        return returnMap;
    }
}
