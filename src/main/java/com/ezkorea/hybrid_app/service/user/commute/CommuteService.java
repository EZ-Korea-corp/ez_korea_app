package com.ezkorea.hybrid_app.service.user.commute;

import com.ezkorea.hybrid_app.domain.myBatis.CommuteMbRepository;
import com.ezkorea.hybrid_app.domain.sale.Payment;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTimeRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommuteService {

    private final CommuteTimeRepository ctRepository;
    private final CommuteMbRepository ctMbRepository;

    /**
     * 출근처리를 위한 메소드
     *
     * @param member          현재 로그인한 유저
     * @param status          출근 : onTime, 퇴근 : offTime, 대기 : away
     * @param currentLocation
     */
    @Transactional
    public Member saveCommuteTime(Member member, String status, String currentLocation) {
        if (status.equals("onTime")) {
            CommuteTime ct = CommuteTime.builder()
                    .date(LocalDate.now())
                    .onTime(LocalDateTime.now())
                    .status(status)
                    .member(member)
                    .onTimeLocation(currentLocation)
                    .build();
            member.addCommuteTime(ctRepository.save(ct));
        } else if (status.equals("offTime")) {
            CommuteTime ct = findCommuteTimeByMember(member);
            ct.setStatus(status);
            ct.setOffTime(LocalDateTime.now());
            ct.setOffTimeLocation(currentLocation);
        }
        return member;
    }

    /**
     * 현재 날짜와 로그인한 유저를 가지고 출근했는지 확인하는 메소드
     * @param member 현재 로그인한 member
     * @return 현재 날짜에 출근한 CommuteTime 반환
     * */
    public CommuteTime findCommuteTimeByMember(Member member) {
        return ctRepository.findByDateAndMember(LocalDate.now(), member);
    }

    public List<Map<String, String>> findCommuteTime(String date) {
        return ctMbRepository.findCommuteTime(date);
    }

    public List<Map<String, String>> findMemberChart() {
        List<Map<String, String>> profileList = ctMbRepository.findMemberChart();
        // 직급명 추가
        profileList.forEach(item -> {
            item.put("ROLE_NM", Role.of(item.get("MEMBER_ROLE")));
        });
        return profileList;
    }
}
