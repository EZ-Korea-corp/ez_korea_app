package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.user.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    List<Member> findAllByRoleAndMemberStatusOrTeam(Role role, MemberStatus status, Team team);

    @Query("SELECT m FROM Member m WHERE (m.role = ?1 and m.team = null) or (m.role = ?1 and m.team = ?2)")
    List<Member> findAllByRoleAndTeamIsNullOrTeam(Role role, Team team);

}
