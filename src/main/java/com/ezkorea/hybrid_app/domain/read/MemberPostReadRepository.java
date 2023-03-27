package com.ezkorea.hybrid_app.domain.read;

import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberPostReadRepository extends JpaRepository<MemberPostRead, Long> {

    boolean existsByNoticeAndMember(Notice notice, Member member);

    List<MemberPostRead> findAllByNotice(Notice notice);

    void deleteAllByMember(Member member);

    List<MemberPostRead> findByMember(Member member);

}
