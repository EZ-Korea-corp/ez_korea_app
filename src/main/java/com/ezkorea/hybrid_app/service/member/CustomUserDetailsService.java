package com.ezkorea.hybrid_app.service.member;

import com.ezkorea.hybrid_app.domain.member.Member;
import com.ezkorea.hybrid_app.domain.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.member.SecurityUser;
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

        if (member.getUsername().contains("master")) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else if (member.getUsername().contains("manage")) {
            authorities.add(new SimpleGrantedAuthority("MANAGER"));
        }
        authorities.add(new SimpleGrantedAuthority("MEMBER"));

        return new SecurityUser(member, authorities);
    }
}
