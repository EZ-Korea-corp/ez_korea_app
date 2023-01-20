package com.ezkorea.hybrid_app.service.member;

import com.ezkorea.hybrid_app.domain.member.Member;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ModelMapper mapper;

    /**
     * 회원가입을 하기 위한 메소드
     *
     * @param dto sign-up.html에서 받아온 정보
     */
    public Member saveNewMember(SignUpDto dto) {
        return mapper.map(dto, Member.class);
    }
}
