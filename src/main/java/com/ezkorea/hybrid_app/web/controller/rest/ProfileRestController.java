package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.ProfileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProfileRestController {

    private final MemberService memberService;
    private final ModelMapper mapper;

    @PutMapping("/settings/password")
    public ResponseEntity<Object> doPasswordSettingUpdate(@AuthenticationPrincipal SecurityUser securityUser,
                                                 @RequestBody Map<String, Object> datum) {

        ProfileDto dto = mapper.map(datum, ProfileDto.class);
        if (!memberService.updateMemberPassword(dto, securityUser.getMember())) {
            return new ResponseEntity<>(Map.of("message", "비밀번호가 일치하지 않습니다."), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."), HttpStatus.OK);
    }

    @PutMapping("/settings/account")
    public ResponseEntity<Object> doProfileSettingUpdate(@AuthenticationPrincipal SecurityUser securityUser,
                                                         @RequestBody ProfileDto dto) {

        memberService.updateMemberInfo(securityUser.getMember(), dto);

        return new ResponseEntity<>(Map.of("message", "정보가 성공적으로 변경되었습니다."), HttpStatus.OK);
    }
}
