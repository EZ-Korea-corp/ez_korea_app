package com.ezkorea.hybrid_app.service.user.team;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
import com.ezkorea.hybrid_app.web.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final MemberService mService;

    @Transactional
    public Team saveNewTeam(TeamDto dto) {
        Team savedTeam = teamRepository.save(Team.builder()
                .division(dto.getDivision())
                .teamName(dto.getTeamName())
                .memberList(dto.getMemberList())
                .build());
        if (dto.getLeader() != null) {
            savedTeam.setLeader(dto.getLeader());
            setLeaderTeam(savedTeam);
        }
        setMemberListTeam(savedTeam);
        return savedTeam;
    }

    public TeamDto createTeamDto(Division division, String teamName, String teamLeader, String teamEmployee) {
        TeamDto teamDto = TeamDto.builder()
                .division(division)
                .teamName(teamName)
                .build();
        if (teamLeader != null) {
            teamDto.setLeader(mService.findByUsername(teamLeader));
        }
        List<Member> memberList = new ArrayList<>();
        for (String employeeUsername : teamEmployee.split(",")) {
            memberList.add(mService.findByUsername(employeeUsername));
        }
        teamDto.setMemberList(memberList);
        return teamDto;
    }

    @Transactional
    public void updateTeam(Long teamId, Division division, String teamName, String teamLeader, String teamEmployee) {
        Team currentTeam = findById(teamId);

        if (teamLeader != null) {
            Member currentTeamLeader = mService.findByUsername(teamLeader);
            currentTeamLeader.setDivision(division);
            currentTeamLeader.setTeam(currentTeam);
            currentTeam.setLeader(currentTeamLeader);
        }

        // 기존 소속 팀원 설정
        for (Member member : currentTeam.getMemberList()) {
            member.setDivision(null);
            member.setTeam(null);
        }

        // 변경된 소속 팀원 설정
        List<Member> memberList = new ArrayList<>();
        for (String employeeUsername : teamEmployee.split(",")) {
            Member currentMember = mService.findByUsername(employeeUsername);
            memberList.add(currentMember);
            currentMember.setDivision(currentTeam.getDivision());
            currentMember.setTeam(currentTeam);
        }

        // 팀 정보 변경
        currentTeam.setTeamName(teamName);
        currentTeam.setDivision(division);
        currentTeam.setMemberList(memberList);
    }

    @Transactional
    public void setLeaderTeam(Team team) {
        team.getLeader().setTeam(team);
        team.getLeader().setDivision(team.getDivision());
    }

    @Transactional
    public void setMemberListTeam(Team team) {
        for (Member member : team.getMemberList()) {
            member.setTeam(team);
            member.setDivision(team.getDivision());
        }
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow( () -> new TeamNotFoundException("팀이 존재하지 않습니다."));
    }
}
