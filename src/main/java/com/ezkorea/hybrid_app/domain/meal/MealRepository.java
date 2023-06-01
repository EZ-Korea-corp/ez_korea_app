package com.ezkorea.hybrid_app.domain.meal;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {
    boolean existsByMemberAndCheckDate(Member member, LocalDate date);

    List<Meal> findAllByCheckDateOrderByCreateDate(LocalDate date);

}
