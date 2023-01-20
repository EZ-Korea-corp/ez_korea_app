package com.ezkorea.hybrid_app.web.exception;

import com.ezkorea.hybrid_app.app.util.Script;
import lombok.extern.slf4j.Slf4j;
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
        return Script.href("/login", e.getMessage());
    }
}
