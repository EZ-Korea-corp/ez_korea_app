package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DivisionRestController {

    private final DivisionService divisionService;
    @PostMapping("/team")
    public HttpStatus saveTeam(@RequestBody Map<String, Object> data,
                               @AuthenticationPrincipal SecurityUser securityUser) {
        String username = (String) data.get("username");

        divisionService.setEmployeeDivision(username, securityUser.getMember());

        return HttpStatus.OK;
    }
}
