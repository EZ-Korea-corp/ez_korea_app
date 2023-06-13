package com.ezkorea.hybrid_app.service.adjustment;

import com.ezkorea.hybrid_app.domain.adjustment.*;
import com.ezkorea.hybrid_app.domain.adjustment.mainDivision.MainDivision;
import com.ezkorea.hybrid_app.domain.myBatis.AdjustmentMbRepository;
import com.ezkorea.hybrid_app.domain.timetable.TimeTableRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import com.ezkorea.hybrid_app.web.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdjustmentService {

    private final AdjustmentMbRepository adjustmentMbRepository;
    private final AdjustmentRepository adjustmentRepository;
    private final TeamService teamService;

    private final String LOW_PERFORM = "LOW_PERFORM";
    private final String DAY_OFF_MEMBER = "DAY_OFF_MEMBER";

    public void saveAdjustment(AdjustmentDto dto) {
        Team team = teamService.findById(dto.getTeamNo());
        dto.setDivisionNo(team.getDivision().getId());

        adjustmentRepository.save(Adjustment.builder()
                .teamNo(dto.getTeamNo())
                .divisionNo(dto.getDivisionNo())
                .dayOffAdj(dto.getDayOffAdj())
                .lowFormAdj(dto.getLowFormAdj())
                .cashAdj(dto.getCashAdj())
                .cardAdj(dto.getCardAdj())
                .teamAvg(dto.getTeamAvg())
                .totalAdj(dto.getTotalAdj())
                .accountAdj(dto.getAccountAdj())
                .contentAdj(dto.getContentAdj())
                .adjDate(dto.getAdjDate())
                .build());
    }

    @Transactional
    public void updateAdjustment(AdjustmentDto dto) {
        Adjustment currentAdj = findByTeamNoAndAdjDate(dto.getTeamNo(), dto.getAdjDate());
        currentAdj.updateAdj(dto);
    }

    public Adjustment findByTeamNoAndAdjDate(Long teamNo, LocalDate date) {
        return adjustmentRepository.findByTeamNoAndAdjDate(teamNo, date)
                .orElseThrow(() -> new TeamNotFoundException("팀을 찾을 수 없습니다."));
    }

    public boolean existsByTeamNoAndAdjDate(Long teamNo, LocalDate date) {
        return adjustmentRepository.existsByTeamNoAndAdjDate(teamNo, date);
    }

    /**
     * Member 객체를 찾는 메소드
     * @param dto 저장(수정)할 AdjustmentDto
     * @param status 상단 private final로 선언된 LOW_PERFORM 변수
     * @return 저조자 혹은 휴무자 객체 List 반환
     */
    public List<Member> findMemberByDto(AdjustmentDto dto, String status) {
        String[] nameArr = null;
        if (status.equals(LOW_PERFORM)){
            nameArr = dto.getLowFormAdj().split(",");
        } else {
            nameArr = dto.getDayOffAdj().split(",");
        }

        Team currentTeam = teamService.findById(dto.getTeamNo());

        Map<String, Member> memberMap = currentTeam.getMemberList().stream()
                .collect(Collectors.toMap(Member::getName, Function.identity()));

        return Arrays.stream(nameArr)
                .map(String::trim)
                .map(memberMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Map<String, String>> findAdjStat(Map map) {
        List<Map<String, String>> list = adjustmentMbRepository.findAdjStat(map);
        for (Map<String, String> item : list) {
            item.put("title", MainDivision.of(item.get("MAIN_DIVISION")));
        }

        return list;
    }

}
