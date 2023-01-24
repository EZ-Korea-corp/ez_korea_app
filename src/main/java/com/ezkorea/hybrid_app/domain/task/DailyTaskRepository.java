package com.ezkorea.hybrid_app.domain.task;

import com.ezkorea.hybrid_app.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    Optional<DailyTask> findByTaskDateAndMember(LocalDate taskDate, Member member);
}
