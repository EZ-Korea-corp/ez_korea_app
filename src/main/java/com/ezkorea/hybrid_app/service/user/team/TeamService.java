package com.ezkorea.hybrid_app.service.user.team;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
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
                .leader(dto.getLeader())
                .memberList(dto.getMemberList())
                .build());
        setMemberTeam(savedTeam);
        return savedTeam;
    }

    @Transactional
    public void setMemberTeam(Team team) {
        team.getLeader().setTeam(team);
        team.getLeader().setDivision(team.getDivision());
        for (Member member : team.getMemberList()) {
            member.setTeam(team);
            member.setDivision(team.getDivision());
        }
    }
}
