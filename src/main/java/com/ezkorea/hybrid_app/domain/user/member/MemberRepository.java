package com.ezkorea.hybrid_app.domain.user.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Member> findAllByRole(Role role);
    List<Member> findAllByRoleAndMemberStatus(Role role, MemberStatus status);

    boolean existsByEmailAndPhone(String email, String phone);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByMemberStatus(MemberStatus status);
    List<Member> findAllByMemberStatusNot(MemberStatus status);

    // IsNull Opt
    List<Member> findAllByRoleAndDivisionIsNull(Role role);
    List<Member> findAllByRoleAndTeamIsNull(Role role);
    List<Member> findAllByRoleAndMemberStatusAndTeamIsNull(Role role, MemberStatus status);
}
