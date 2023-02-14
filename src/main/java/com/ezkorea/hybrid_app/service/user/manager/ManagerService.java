package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberService mService;
    private final DivisionService dService;
    private final TeamService tService;

    public List<Member> findAllMemberByRole(Role role) {
        return mService.findByRole(role);
    }

    public List<Member> findAllMemberByRoleAndDivisionIsNull(Role role) {
        return mService.findByRoleAndDivisionIsNull(role);
    }

    public List<Member> findAllMemberByRoleAndTeamIsNull(Role role) {
        return mService.findByRoleAndTeamIsNull(role);
    }

    public List<Member> findAllMemberByRoleAndStatus(Role role, MemberStatus status) {
        return mService.findByRoleAndStatus(role, status);
    }

    public List<Member> findAllMemberByRoleAndStatusAndTeamIsNull(Role role, MemberStatus status) {
        return mService.findByRoleAndStatusAndTeamIsNull(role, status);
    }

    @Transactional
    public void updateMemberRole(String username, Role role, MemberStatus status) {
        Member currentMember = mService.findByUsername(username);
        /*switch (status) {
            case AWAY -> {

            }
        }*/
        mService.updateMemberRole(currentMember, role, status);
    }

    public List<Member> findAllMemberByStatus(MemberStatus status) {
        return mService.findAllMemberByStatus(status);
    }

    public List<Member> findAllMemberExcludeAwait() {
        return mService.findAllMemberExcludeStatus(MemberStatus.AWAIT);
    }

    public void updateMemberStatus(Long id, MemberStatus status) {
        Member currentMember = mService.findMemberById(id);
        mService.updateMemberStatus(currentMember, status);
    }

    public Division saveNewDivision(String teamName, String teamGm) {
        Member currentMember = mService.findByUsername(teamGm);
        DivisionDto dto = new DivisionDto(teamName, currentMember);
        return dService.saveNewDivision(dto);
    }

    public List<Division> findAllDivision() {
        return dService.findAllDivision();
    }

    public Team saveNewTeam(String divisionName, String teamName, String teamLeader, String teamEmployee) {
        List<Member> memberList = new ArrayList<>();
        for (String employeeUsername : teamEmployee.split(",")) {
            memberList.add(mService.findByUsername(employeeUsername));
        }
        return tService.saveNewTeam(TeamDto.builder()
                .division(dService.findDivisionByDivisionName(divisionName))
                .teamName(teamName)
                .leader(mService.findByUsername(teamLeader))
                .memberList(memberList)
                .build());
    }

    public Division findDivisionById(Long id) {
        return dService.findDivisionById(id);
    }
}
