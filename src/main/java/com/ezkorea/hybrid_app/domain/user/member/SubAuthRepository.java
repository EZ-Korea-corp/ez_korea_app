package com.ezkorea.hybrid_app.domain.user.member;

import org.springframework.data.jpa.repository.JpaRepository;
public interface SubAuthRepository extends JpaRepository<SubAuth, Long> {
    SubAuth findByMember(Member member);
}
