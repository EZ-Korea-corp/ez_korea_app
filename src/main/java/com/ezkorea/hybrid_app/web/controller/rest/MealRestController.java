package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.domain.meal.Meal;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.meal.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MealRestController {

    private final MealService mealService;

    @PostMapping("/meal")
    public ResponseData.ApiResult<?> checkMeal(@AuthenticationPrincipal SecurityUser securityUser) {
        Member currentMember = securityUser.getMember();
        if (mealService.existsMealChk(currentMember)) {
            return ResponseData.error("이미 식사체크 되었습니다.", HttpStatus.BAD_REQUEST);
        }
        mealService.saveMealChk(currentMember);
        return ResponseData.success(null, "식사체크 되었습니다.");
    }
}
