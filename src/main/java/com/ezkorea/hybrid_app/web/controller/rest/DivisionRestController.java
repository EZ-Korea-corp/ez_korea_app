package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper mapper;

    @PostMapping("/team")
    public HttpStatus saveTeam(@RequestBody Map<String, Object> data,
                               @AuthenticationPrincipal SecurityUser securityUser) {

        DivisionDto dto = mapper.map(data, DivisionDto.class);
        log.info("dto.getPosition()={}", dto.getPosition());
        log.info("dto.getUsername()={}", dto.getUsername());
        log.info("dto.getStatus()={}", dto.getStatus());

        String username = (String) data.get("username");

        divisionService.setEmployeeDivision(dto, securityUser.getMember());

        return HttpStatus.OK;
    }
}
