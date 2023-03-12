package com.ezkorea.hybrid_app.web.controller.rest.manager;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerMemberRestController {

    private final TeamService tService;
    private final MemberService mService;
    private final DivisionService dService;
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

    @PutMapping("/member")
    public ResponseEntity<Object> updateMemberRole(@RequestBody Map<String, Object> datum) {
        MemberUpdateDto dto = mapper.map(datum, MemberUpdateDto.class);

        Member currentMember = mService.findByUsername(dto.getUsername());

        // 회원 직급이 바뀔 경우 팀, 소속 제거
        if (!currentMember.getRole().equals(dto.getMemberRole())) {
            if (currentMember.getRole().equals(Role.ROLE_EMPLOYEE)) {
                tService.removeTeamMember(currentMember);
            } else if (currentMember.getRole().equals(Role.ROLE_LEADER)) {
                tService.removeTeamLeader(currentMember);
            } else if (currentMember.getRole().equals(Role.ROLE_GM)) {
                dService.removeDivisionLeader(currentMember);
            }
        }

        mService.updateMemberSubAuth(dto.getUsername(), dto);
        mService.updateMemberRole(dto.getUsername(), dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

}
