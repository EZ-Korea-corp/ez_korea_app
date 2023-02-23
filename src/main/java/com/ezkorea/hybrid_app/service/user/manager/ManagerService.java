package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.myBatis.CommuteMbRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberService mService;
    private final DivisionService dService;
    private final TeamService tService;

    private final CommuteMbRepository commuteMbRepository;
    private final DailyTaskRepository dailyTaskRepository;
    private final SaleMbRepository saleMbRepository;

    public List<Member> findAllMemberByRole(Role role) {
        return mService.findByRole(role);
    }

    public List<Member> findAllMemberByRoleAndDivisionIsNull(Role role) {
        return mService.findByRoleAndDivisionIsNull(role);
    }

    public List<Member> findAllMemberByRoleAndTeamIsNull(Role role) {
        return mService.findByRoleAndTeamIsNull(role);
    }

    public List<Member> findAllByRoleAndTeamIsNullOrTeam(Role role, Team team) {
        return mService.findAllByRoleAndTeamIsNullOrTeam(role, team);
    }

    public Team findTeamById(Long id) {
        return tService.findById(id);
    }

    public List<Member> findAllMemberByRoleAndStatusOrTeam(Role role, MemberStatus status, Team team) {
        return mService.findByRoleAndStatusAndTeam(role, status, team);
    }

    public List<Member> findAllMemberByRoleAndStatus(Role role, MemberStatus status) {
        return mService.findByRoleAndStatus(role, status);
    }

    public List<Member> findAllMemberByRoleAndStatusAndTeamIsNull(Role role, MemberStatus status) {
        return mService.findByRoleAndStatusAndTeamIsNull(role, status);
    }

    @Transactional
    public void updateMemberSubAuth(String username, String memberPostAuth, String memberInputAuth) {
        Member currentMember = mService.findByUsername(username);

        boolean postAuth = memberPostAuth != null;
        boolean inputAuth = memberInputAuth != null;

        mService.updateMemberSubAuth(currentMember, postAuth, inputAuth);
    }

    @Transactional
    public void updateMemberRole(String username, Role role, MemberStatus status) {
        Member currentMember = mService.findByUsername(username);
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

    @Transactional
    public void updateDivision(Long divisionId, String teamName, String teamGm) {
        Division currentDivision = dService.findDivisionById(divisionId);
        currentDivision.setDivisionName(teamName);
        currentDivision.setLeader(mService.findByUsername(teamGm));
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

    @Transactional
    public void updateTeam(Long teamId, String divisionName, String teamName, String teamLeader, String teamEmployee) {
        Team currentTeam = tService.findById(teamId);
        Member currentTeamLeader = mService.findByUsername(teamLeader);

        currentTeamLeader.setDivision(currentTeam.getDivision());
        currentTeamLeader.setTeam(currentTeam);

        for (Member member : currentTeam.getMemberList()) {
            member.setDivision(null);
            member.setTeam(null);
        }

        List<Member> memberList = new ArrayList<>();
        for (String employeeUsername : teamEmployee.split(",")) {
            Member currentMember = mService.findByUsername(employeeUsername);
            memberList.add(currentMember);
            currentMember.setDivision(currentTeam.getDivision());
            currentMember.setTeam(currentTeam);
        }

        currentTeam.setDivision(dService.findDivisionByDivisionName(divisionName));
        currentTeam.setTeamName(teamName);
        currentTeam.setLeader(currentTeamLeader);
        currentTeam.setMemberList(memberList);

    }

    public Division findDivisionById(Long id) {
        return dService.findDivisionById(id);
    }

    public List<Map<String, String>> findTaskDateList(Map<String, Object> data) {
        return commuteMbRepository.findTaskDateList(data);
    }


    public List<DailyTask> findTaskList(String date, Long id) {
        LocalDate searchDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return dailyTaskRepository.findByTaskDateAndMemberId(searchDate, id);
    }

    public Object findTotalStat(Map<String, Object> data) {
        return saleMbRepository.findTotalStat(data);
    }
}
