package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    boolean existsByUsername(String username);

    List<Member> findAllByRole(Role role);

    boolean existsByEmailAndPhone(String email, String phone);

    Optional<Member> findByEmail(String email);

    List<Member> findAllByMemberStatus(MemberStatus status);
    List<Member> findAllByMemberStatusNot(MemberStatus status);

    // IsNull Opt
    @Query("SELECT m FROM Member m WHERE m.memberStatus = ?2 and ((m.role = ?1 and m.division = null) or (m.role = ?1 and m.division = ?3))")
    List<Member> findByRoleAndDivisionAndDivisionAndDivisionNull(Role role, MemberStatus status, Division division);
    List<Member> findAllByRoleAndMemberStatusAndDivisionIsNull(Role role, MemberStatus status);
    List<Member> findAllByRoleAndTeamIsNull(Role role);
    List<Member> findAllByRoleAndMemberStatusAndTeamIsNull(Role role, MemberStatus status);

    @Query("SELECT m FROM Member m WHERE m.memberStatus = ?3 and ((m.role = ?1 and m.team = null) or (m.role = ?1 and m.team = ?2))")
    List<Member> findAllByRoleAndTeamIsNullOrTeam(Role role, Team team, MemberStatus memberStatus);

    // QA
    List<Member> findAllByRoleAndMemberStatus(Role role, MemberStatus status);
}
