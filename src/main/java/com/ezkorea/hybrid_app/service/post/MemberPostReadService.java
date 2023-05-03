package com.ezkorea.hybrid_app.service.post;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.domain.read.MemberPostRead;
import com.ezkorea.hybrid_app.domain.read.MemberPostReadRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.service.notiece.NoticeService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberPostReadService {

    private final MemberPostReadRepository mprRepository;
    private final MemberService memberService;
    private final NoticeRepository noticeRepository;

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
    public List<MemberDto> findNotReadMemberList(Notice curerntNotice) {
        List<Member> memberList = memberService.findAllMemberByStatus(MemberStatus.FULL_TIME);
        List<MemberPostRead> allByNotice = mprRepository.findAllByNotice(curerntNotice);

        Set<Member> readMember = allByNotice.stream()
                .map(MemberPostRead::getMember)
                .collect(Collectors.toSet());

        memberList.removeAll(readMember);
        memberList.removeIf(member -> member.getUsername().equals("dev"));

        return memberList.stream()
                .map(Member::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Notice> findNotReadListByMember(Member member) {
        List<MemberPostRead> mprList = mprRepository.findByMember(member);
        List<Notice> findAllNotice = noticeRepository.findAll();
        for (MemberPostRead memberPostRead : mprList) {
            findAllNotice.remove(memberPostRead.getNotice());
        }
        return findAllNotice;
    }
}
