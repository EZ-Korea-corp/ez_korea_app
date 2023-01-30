package com.ezkorea.hybrid_app.domain.user.team;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByDivision(Division division);

    void deleteByDivision(Division division);
}
