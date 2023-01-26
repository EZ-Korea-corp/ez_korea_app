package com.ezkorea.hybrid_app.service.user.member;

import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTimeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final ModelMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final CommuteTimeRepository ctRepository;

    private final CommuteService commuteService;


    /**
     * 회원가입을 하기 위한 메소드
     * @param dto sign-up.html에서 받아온 정보
     */
    public Member saveNewMember(SignUpDto dto) {
        dto.setPassword(passwordEncode(dto.getPassword()));
        return memberRepository.save(mapper.map(dto, Member.class));
    }

    /**
     * bcrypt로 인코딩하기 위한 메소드
     * @param password 인코딩 되지 않은 password
     * @return 인코딩 된 password
     */
    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * username을 통해 정확한 Member를 찾기 위한 메소드
     * @param username 검색할 username
     * @exception UsernameNotFoundException 유저가 없을 시 발생
     * @return username에 맞는 Member 객체
     */
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("해당되는 유저가 없습니다."));
    }

    /**
     * id를 통해 Member를 찾는 메소드
     * @param id 검색할 id
     * @exception IdNotFoundException 유저가 없을 시 발생
     * @return id에 맞는 Member 객체
     * */
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "를 찾을 수 없습니다."));
    }

    /**
     * 회원 정보 갱신을 위한 메소드
     * @param member 현재 로그인된 Member
     * */
    public void forceAuthentication(Member member) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (member.getUsername().contains("master")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else if (member.getUsername().contains("manage")) {
            authorities.add(new SimpleGrantedAuthority("MANAGER"));
        }
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        SecurityUser securityUser = new SecurityUser(member, authorities);

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    /**
     * 오늘 출근했는지 확인하는 메소드
     * @param member 현재 로그인한 멤버
     * @return 출근 여부
     */
    public boolean isOnTime(Member member) {
        return ctRepository.existsByDateAndMember(LocalDate.now(), member);
    }

    @Transactional
    public void setCommuteTime(Member member, String status) {
        Member currentMember = findByUsername(member.getUsername());
        forceAuthentication(memberRepository
                .save(commuteService
                        .saveCommuteTime(currentMember, status)
                )
        );
    }
}
