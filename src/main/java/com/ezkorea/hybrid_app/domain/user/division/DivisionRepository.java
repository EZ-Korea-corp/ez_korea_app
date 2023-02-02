package com.ezkorea.hybrid_app.domain.user.division;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    Optional<Division> findByMember(Member member);

    List<Division> findAllByMember(Member member);

    void deleteByTeam(Team team);

    void deleteByMember(Member leader);

    boolean existsByMember(Member leader);

    void deleteAllByTeam(Team team);

    List<Division> findAllByTeam(Team team);
}
