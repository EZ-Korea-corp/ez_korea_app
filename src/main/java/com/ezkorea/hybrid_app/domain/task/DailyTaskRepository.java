package com.ezkorea.hybrid_app.domain.task;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTaskRepository extends JpaRepository<DailyTask, Long> {
    Optional<DailyTask> findByTaskDateAndMember(LocalDate taskDate, Member member);

    List<DailyTask> findAllByTaskDateAndGasStation(LocalDate taskDate, GasStation station);
}
