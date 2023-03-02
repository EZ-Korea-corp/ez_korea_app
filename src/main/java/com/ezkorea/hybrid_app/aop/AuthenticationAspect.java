package com.ezkorea.hybrid_app.aop;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class AuthenticationAspect {

    private final MemberService memberService;

    public AuthenticationAspect(MemberService memberService) {
        this.memberService = memberService;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {}

    /*@Pointcut("@annotation(org.springframework.stereotype.Controller))")
    public void getController() {}*/

    @Pointcut("!execution(* com.ezkorea.hybrid_app.web.controller.mapping.MemberController.showLoginPage())")
    public void excludeLoginPage() {}

    @Pointcut("!execution(* com.ezkorea.hybrid_app.web.controller.mapping.MemberController.showSignUpPage())")
    public void excludeSignUpPage() {}

    @Around("getMapping() && excludeLoginPage() && excludeSignUpPage()")
    public Object authenticationReset(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 처리할 내용
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member currentMember = memberService.findByUsername(username);
        memberService.forceAuthentication(currentMember);

        Object result = joinPoint.proceed();

        // 메소드 실행 후 처리할 내용

        return result;
    }

}
