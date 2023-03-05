package com.ezkorea.hybrid_app.domain.user.team;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByLeader(Member member);

    Team findByLeader(Member member);
}
