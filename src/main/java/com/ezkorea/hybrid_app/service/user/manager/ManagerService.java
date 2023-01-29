package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.user.division.Position;
import com.ezkorea.hybrid_app.domain.user.division.Status;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberService mService;
    private final TeamService tService;
    private final DivisionService dService;

    public List<Member> findAllMemberByRole(Role role) {
        return mService.findByRole(role);
    }

    public void updateMemberRole(String username, Role role) {
        Member currentMember = mService.findByUsername(username);
        mService.updateMemberRole(currentMember, role);
        if (role.equals(Role.ROLE_LEADER)) {
            Team newTeam = tService.saveNewTeam(username);
            DivisionDto dto = new DivisionDto();
            dto.setPosition(Position.LEADER);
            dto.setStatus(Status.COMPLETE);
            dService.saveNewDivision(newTeam, currentMember, dto);
        }
    }

    public List<Member> findAllMember() {
        return mService.findAllMember();
    }
}
