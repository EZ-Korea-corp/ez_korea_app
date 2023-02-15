package com.ezkorea.hybrid_app.service.member;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Profile("${test}")
class MemberServiceTest {

    @Autowired
    ModelMapper mapper;

    @Autowired
    MemberService memberService;

    @Test
    void findMemberByUsernameTest() {
        long start = System.currentTimeMillis();
        Member member = memberService.findByUsername("ez_dev_team_master");
        long end = System.currentTimeMillis();
        System.out.println("JPA 실행 시간 : " + (end - start)/1000.0);
        assertThat(member.getName()).isEqualTo("개발팀");
    }

    @Test
    void findMemberByIdTest() {
        long start = System.currentTimeMillis();
        Member member = memberService.findMemberById(1L);
        long end = System.currentTimeMillis();
        System.out.println("JPA 실행 시간 : " + (end - start)/1000.0);
        assertThat(member.getName()).isEqualTo("개발팀");
    }
}