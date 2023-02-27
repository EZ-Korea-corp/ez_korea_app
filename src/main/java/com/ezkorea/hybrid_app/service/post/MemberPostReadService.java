package com.ezkorea.hybrid_app.service.post;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.read.MemberPostRead;
import com.ezkorea.hybrid_app.domain.read.MemberPostReadRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberPostReadService {

    private final MemberPostReadRepository mprRepository;
    private final MemberService memberService;

    @Transactional
    public void saveReadInfo(Notice currentNotice, Member currentMember) {
        if (!mprRepository.existsByNoticeAndMember(currentNotice, currentMember)) {
            MemberPostRead savedMpr = mprRepository.save(MemberPostRead.builder()
                    .notice(currentNotice)
                    .member(currentMember)
                    .build());
            currentNotice.getReadList().add(savedMpr);
        }

    }

    @Transactional(readOnly = true)
    public List<MemberPostRead> findReadMemberList(Notice curerntNotice) {
        return mprRepository.findAllByNotice(curerntNotice);
    }

    @Transactional(readOnly = true)
    public List<Member> findNotReadMemberList(Notice curerntNotice) {
        List<Member> memberList = memberService.findAllMemberByStatus(MemberStatus.FULL_TIME);
        List<MemberPostRead> allByNotice = mprRepository.findAllByNotice(curerntNotice);

        for (MemberPostRead mpr : allByNotice) {
            memberList.remove(mpr.getMember());
        }
        memberList.remove(memberService.findByUsername("dev"));
        return memberList;
    }
}
