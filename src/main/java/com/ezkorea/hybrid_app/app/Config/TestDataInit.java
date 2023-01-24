package com.ezkorea.hybrid_app.app.Config;

import com.ezkorea.hybrid_app.domain.member.MemberRepository;
import com.ezkorea.hybrid_app.service.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @PostConstruct
    public void testMemberDataInit() {
        if (!memberRepository.existsByUsername("01012341234")) {
            memberService.saveNewMember(makeNewMember("01012341234", "김기만"));
        }
        if (!memberRepository.existsByUsername("01011112222")) {
            memberService.saveNewMember(makeNewMember("01011112222", "남궁성"));
        }
    }

    public SignUpDto makeNewMember(String username, String name) {
        SignUpDto newDto = new SignUpDto();
        newDto.setUsername(username);
        newDto.setPassword("1234");
        newDto.setName(name);
        newDto.setSex("MALE");

        return newDto;
    }
}
