package com.ezkorea.hybrid_app.domain.user.division;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    Division findByDivisionName(String divisionName);

    boolean existsByLeader(Member leader);

    Division findByLeader(Member leader);

    List<Division> findAllByLeader(Member leader);
}
