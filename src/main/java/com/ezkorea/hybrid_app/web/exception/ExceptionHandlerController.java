package com.ezkorea.hybrid_app.web.exception;

import com.ezkorea.hybrid_app.app.util.Script;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFoundError(NoHandlerFoundException e) {
        log.error("NoHandlerFoundException={}", e);
        return Script.href("/", "존재하지 않는 페이지입니다.");
    }

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

    @ExceptionHandler(value = TeamNotFoundException.class)
    public @ResponseBody String teamNotFoundException(TeamNotFoundException e) {
        log.error("TeamNotFoundException={}", e);

        return Script.href("/", e.getMessage());
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public @ResponseBody String locationNotFoundException(JsonProcessingException e) {
        log.error("JsonProcessingException={}", e);

        return Script.href("/", "위치 정보를 찾을 수 없습니다. 새로고침 후 다시 시도해주세요.");
    }
}
