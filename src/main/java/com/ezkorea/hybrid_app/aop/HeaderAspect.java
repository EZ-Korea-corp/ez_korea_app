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
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

@Aspect
@Slf4j
@Component
public class HeaderAspect {

    @Pointcut("execution(* com.ezkorea.hybrid_app.web.controller.mapping.SaleController.*(..))")
    public void saleControllerModel() {}

    @Around("execution(* com.ezkorea.hybrid_app.web.controller.mapping.SaleController.*(..))")
    public Object salesAttribute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 처리할 내용
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                model.addAttribute("currentPage", "sales");
            }
        }

        Object result = joinPoint.proceed();

        return result;
    }

    @Around("execution(* com.ezkorea.hybrid_app.web.controller.mapping.GasStationController.*(..))")
    public Object stationAttribute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 처리할 내용
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                model.addAttribute("currentPage", "station");
            }
        }

        Object result = joinPoint.proceed();

        return result;
    }

    @Around("execution(* com.ezkorea.hybrid_app.web.controller.mapping.ChartController.*(..))")
    public Object chartAttribute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 처리할 내용
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                model.addAttribute("currentPage", "chart");
            }
        }

        Object result = joinPoint.proceed();

        return result;
    }

    @Around("execution(* com.ezkorea.hybrid_app.web.controller.mapping.ExpensesController.*(..))")
    public Object expensesAttribute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 실행 전 처리할 내용
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof Model model) {
                model.addAttribute("currentPage", "expenses");
            }
        }

        Object result = joinPoint.proceed();

        return result;
    }

}
