package com.ezkorea.hybrid_app.service.member;

import com.ezkorea.hybrid_app.domain.member.Member;
import com.ezkorea.hybrid_app.domain.member.MemberRepository;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;


    /**
     * 회원가입을 하기 위한 메소드
     * @param dto sign-up.html에서 받아온 정보
     */
    public Member saveNewMember(SignUpDto dto) {
        dto.setPassword(passwordEncode(dto.getPassword()));
        return memberRepository.save(mapper.map(dto, Member.class));
    }

    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }
}
