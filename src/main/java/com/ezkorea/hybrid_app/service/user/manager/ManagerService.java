package com.ezkorea.hybrid_app.service.user.manager;

import com.ezkorea.hybrid_app.domain.myBatis.CommuteMbRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.Payment;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ManagerService {

    private final CommuteMbRepository commuteMbRepository;
    private final DailyTaskRepository dailyTaskRepository;
    private final SaleMbRepository saleMbRepository;

    public List<Map<String, String>> findTaskDateList(Map<String, Object> data) {
        return commuteMbRepository.findTaskDateList(data);
    }


    public List<DailyTask> findTaskList(String date, Long id) {
        LocalDate searchDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        return dailyTaskRepository.findByTaskDateAndMemberId(searchDate, id);
    }

    public Map<String, Object> findTotalStat(Map<String, Object> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> priceList = saleMbRepository.findTablePrice(paramMap);
        // 결제수단명 추가
        priceList.forEach(item -> {
            item.put("NAME", Payment.of((String)item.get("PAYMENT")));
        });

        returnMap.put("countList", saleMbRepository.findTotalStat(paramMap));
        returnMap.put("priceList", priceList);
        return returnMap;
    }
}
