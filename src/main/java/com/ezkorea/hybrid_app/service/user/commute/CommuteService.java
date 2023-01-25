package com.ezkorea.hybrid_app.service.user.commute;

import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTimeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommuteService {

    private final CommuteTimeRepository ctRepository;
    private final SaleService saleService;

    /**
     * 출근처리를 위한 메소드
     * @param member 현재 로그인한 유저
     * @param status 출근 : onTime, 퇴근 : offTime, 대기 : away
     * */
    @Transactional
    public Member saveCommuteTime(Member member, String status) {
        if (status.equals("onTime")) {
            CommuteTime ct = CommuteTime.builder()
                    .date(LocalDate.now())
                    .onTime(LocalDateTime.now())
                    .status(status)
                    .member(member)
                    .build();
            member.addCommuteTime(ctRepository.save(ct));
            saleService.saveDailyTask(member);
        } else if (status.equals("offTime")) {
            CommuteTime ct = findCommuteTimeByMember(member);
            ct.setStatus(status);
            ct.setOffTime(LocalDateTime.now());
        } else {
            CommuteTime ct = findCommuteTimeByMember(member);
            ct.setStatus("away");
        }
        return member;
    }

    public CommuteTime findCommuteTimeByMember(Member member) {
        return ctRepository.findByDateAndMember(LocalDate.now(), member);
    }
}
