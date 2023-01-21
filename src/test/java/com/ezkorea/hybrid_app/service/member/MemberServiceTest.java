package com.ezkorea.hybrid_app.service.member;

import com.ezkorea.hybrid_app.domain.member.Member;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    ModelMapper mapper;

    @Autowired
    MemberService memberService;

    SignUpDto saveDtoInfo() {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("테스트");
        signUpDto.setSex("MALE");
        signUpDto.setPassword("1234");
        signUpDto.setUsername("01012341234");
        return signUpDto;
    }

    @BeforeEach
    void saveTest() {

        Member member = memberService.saveNewMember(saveDtoInfo());
        System.out.println("member = " + member.getPassword());
        assertThat(member.getName()).isEqualTo("테스트");

    }

    @Test
    void findByUsernameTest() {
        Member member = memberService.findByUsername("01012341234");
        assertThat(member.getName()).isEqualTo("테스트");
    }
}