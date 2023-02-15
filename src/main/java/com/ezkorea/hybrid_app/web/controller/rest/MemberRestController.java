package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.FindPasswordDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    @GetMapping("/signup/{username}")
    public boolean doFindExistUsername(@PathVariable String username) {
        return memberService.existsMemberByUsername(username);
    }

    @PostMapping("/findPassword")
    public boolean doFindExistUserInfo(@RequestBody FindPasswordDto dto) {

        if (!memberService.existsMemberByEmailAndPhone(dto)) {
            return false;
        }

        memberService.sendTempPassword(dto);

        return true;
    }
}
