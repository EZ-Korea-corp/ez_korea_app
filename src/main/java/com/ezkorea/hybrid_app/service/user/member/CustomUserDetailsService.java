package com.ezkorea.hybrid_app.service.user.member;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("사원번호를 찾을 수 없습니다.") );
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (member.getUsername().contains("ez_dev_team_master")) {
            for (Role value : Role.values()) {
                if (!value.equals(Role.valueOf("ROLE_EMPLOYEE"))) {
                    authorities.add(new SimpleGrantedAuthority(value.toString()));
                }
            }
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));

        return new SecurityUser(member, authorities);
    }
}
