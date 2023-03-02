package com.ezkorea.hybrid_app.service.user.team;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
import com.ezkorea.hybrid_app.web.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

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
