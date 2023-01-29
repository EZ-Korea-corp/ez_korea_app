package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final MemberService mService;
    private final TeamService tService;
    private final DivisionService dService;

    public List<Member> findAllMemberByRole(Role role) {
        return mService.findByRole(role);
    }

    public void updateMemberRole(String username, Role role) {
        mService.updateMemberRole(mService.findByUsername(username), role);
    }

}
