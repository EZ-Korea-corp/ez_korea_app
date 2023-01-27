package com.ezkorea.hybrid_app.service.user.team;

import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {

    private final TeamRepository teamRepository;

    public Team saveNewTeam(String name) {
        return teamRepository.save(Team.builder()
                .name(name)
                .build());
    }

}
