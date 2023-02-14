package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberService mService;
    private final DivisionService dService;

    public List<Member> findAllMemberByRole(Role role) {
        return mService.findByRole(role);
    }

    public List<Member> findAllMemberByRoleAndStatus(Role role, MemberStatus status) {
        return mService.findByRoleAndStatus(role, status);
    }

    @Transactional
    public void updateMemberRole(String username, Role role, MemberStatus status) {
        Member currentMember = mService.findByUsername(username);
        /*switch (status) {
            case AWAY -> {

            }
        }*/
        mService.updateMemberRole(currentMember, role, status);
    }

    public List<Member> findAllMemberByStatus(MemberStatus status) {
        return mService.findAllMemberByStatus(status);
    }

    public List<Member> findAllMemberExcludeAwait() {
        return mService.findAllMemberExcludeStatus(MemberStatus.AWAIT);
    }

    public void updateMemberStatus(Long id, MemberStatus status) {
        Member currentMember = mService.findMemberById(id);
        mService.updateMemberStatus(currentMember, status);
    }
}
