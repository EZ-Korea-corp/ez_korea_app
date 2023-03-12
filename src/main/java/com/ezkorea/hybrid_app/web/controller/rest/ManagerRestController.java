package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SubAuthRepository;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
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

    private final TeamService tService;
    private final MemberService mService;
    private final DivisionService dService;
    private final SaleService saleService;
    private final ModelMapper mapper;

    @PutMapping("/approval")
    public ResponseEntity<Object> updateMemberApproval(@RequestBody Map<String, Object> datum) {
        String idStr = (String) datum.get("id");
        mService.updateMemberStatus(Long.valueOf(idStr), MemberStatus.FULL_TIME);
        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @DeleteMapping("/reject")
    public ResponseEntity<Object> deleteMember(@RequestBody Map<String, Long> datum) {
        mService.deleteMember(datum.get("id"));
        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/team")
    public ResponseEntity<Object> createNewTeam(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());
        String teamName = (String) datum.get("teamName");
        String divisionName = (String) datum.get("teamGM");
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");

        if (divisionName == null) {
            return new ResponseEntity<>(Map.of("message", "지점장이 없는 지점을 새로 만든 뒤 시도해주세요."), HttpStatus.BAD_REQUEST);
        }
        if (teamEmployee == null) {
            return new ResponseEntity<>(Map.of("message", "팀원을 최소 1명 선택해주세요."), HttpStatus.BAD_REQUEST);
        }

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

        if (teamEmployee == null) {
            return new ResponseEntity<>(Map.of("message", "팀원을 최소 1명 선택해주세요."), HttpStatus.BAD_REQUEST);
        }

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

        Member currentMember = mService.findByUsername(username);
        if (currentMember.getTeam() != null) {
            return new ResponseEntity<>(Map.of("message", "소속된 팀에서 제거한 뒤 진행해주세요.(팀명 : %s)".formatted(currentMember.getTeam().getTeamName())), HttpStatus.BAD_REQUEST);
        }

        mService.updateMemberSubAuth(username, memberPostAuth, memberInputAuth);
        mService.updateMemberRole(username, Role.valueOf(memberRole), MemberStatus.valueOf(memberStatus));

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PostMapping("/taskList")
    public Map<String, Object> findTaskList(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("list", saleService.findTaskDateList(data));

        return returnMap;
    }

    @PostMapping("/totalStat")
    public Map<String, Object> findTotalStat(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = saleService.findTotalStat(data);
        return returnMap;
    }
}
