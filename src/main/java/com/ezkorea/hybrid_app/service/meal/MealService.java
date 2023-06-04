package com.ezkorea.hybrid_app.service.meal;

import com.ezkorea.hybrid_app.domain.meal.Meal;
import com.ezkorea.hybrid_app.domain.meal.MealRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealService {
    private final MealRepository mealRepository;
    public Meal saveMealChk(Member member) {
        return mealRepository.save(Meal.builder()
                .member(member)
                .build());
    }

    public boolean existsMealChk(Member member) {
        return mealRepository.existsByMemberAndCheckDate(member, LocalDate.now());
    }

    public List<Meal> findAllByCheckDate(LocalDate localDate) {
        return mealRepository.findAllByCheckDateOrderByCreateDate(localDate);
    }
}
