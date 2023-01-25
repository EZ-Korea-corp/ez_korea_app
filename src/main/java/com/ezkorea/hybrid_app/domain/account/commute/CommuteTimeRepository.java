package com.ezkorea.hybrid_app.domain.account.commute;

import com.ezkorea.hybrid_app.domain.account.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface CommuteTimeRepository extends JpaRepository<CommuteTime, Long> {

    boolean existsByDateAndMember(LocalDate date, Member member);

    CommuteTime findByDateAndMember(LocalDate date, Member member);
}
