package com.ezkorea.hybrid_app.domain.user.division;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    Optional<Division> findByMember(Member member);

}
