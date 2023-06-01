package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCost;
import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCostRepository;
import com.ezkorea.hybrid_app.domain.myBatis.AdjustMentMbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustMentService {

    private final AdjustMentMbRepository adjustMentMbRepository;

    public Map<String, String> findTeamAdjustMentDefault(Long teamId) {
        Map<String, Long> paramMap = new HashMap<>();
        paramMap.put("teamId", teamId);

        return adjustMentMbRepository.findTeamAdjustMentDefault(paramMap);
    }

}
