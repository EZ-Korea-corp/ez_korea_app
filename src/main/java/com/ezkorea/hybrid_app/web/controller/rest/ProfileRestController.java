package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/profile")
public class ProfileRestController {

    private final MemberService memberService;
    private final ModelMapper mapper;

    @PutMapping("/setting")
    public HttpStatus doProfileSettingUpdate(@AuthenticationPrincipal SecurityUser securityUser,
                                             @RequestBody Map<String, Object> datum) {

        ProfileDto dto = mapper.map(datum, ProfileDto.class);
        if (!memberService.updateMemberProfile(dto, securityUser.getMember())) {
            return HttpStatus.BAD_REQUEST;
        }
        return HttpStatus.OK;
    }
}
