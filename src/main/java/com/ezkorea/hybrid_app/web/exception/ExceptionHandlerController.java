package com.ezkorea.hybrid_app.web.exception;

import com.ezkorea.hybrid_app.app.util.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(value = UsernameNotFoundException.class)
    public @ResponseBody String notExistId(UsernameNotFoundException e) {
        log.error("UsernameNotFoundException={}", e);

        return Script.href("/", e.getMessage());
    }

    @ExceptionHandler(value = MemberNotFoundException.class)
    public @ResponseBody String notExistId(MemberNotFoundException e) {
        log.error("MemberNotFoundException={}", e);

        return Script.href("/", e.getMessage());
    }

    @ExceptionHandler(value = GasStationNotFoundException.class)
    public @ResponseBody String notExistGasStaion(GasStationNotFoundException e) {
        log.error("GasStationNotFoundException={}", e);

        return Script.href("/", e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public @ResponseBody String accessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException={}", e);

        return Script.href("/", e.getMessage());
    }

    @ExceptionHandler(value = DivisionNotFoundException.class)
    public @ResponseBody String divisionNotFoundException(DivisionNotFoundException e) {
        log.error("DivisionNotFoundException={}", e);

        return Script.href("/", e.getMessage());
    }
}
