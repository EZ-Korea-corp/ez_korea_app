package com.ezkorea.hybrid_app.service.user.team;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.web.exception.DivisionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    public Team saveNewTeam(Member member) {
        return teamRepository.save(Team.builder()
                .name(member.getName())
                .division(member.getDivision())
                .build());
    }

    public Team findTeamByDivision(Division division) {
        return teamRepository.findByDivision(division)
                .orElseThrow( () -> new DivisionNotFoundException("소속을 찾을 수 없습니다."));
    }

    public void deleteByDivision(Division division) {
        teamRepository.deleteByDivision(division);
    }
}
