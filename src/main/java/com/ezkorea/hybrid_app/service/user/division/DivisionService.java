package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.exception.DivisionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionService {

    private final DivisionRepository divisionRepository;
    private final MemberService mService;

    @Transactional
    public Division saveNewDivision(DivisionDto dto) {
        if (existsDivisionByLeader(dto.getTeamGm())) {
            return findDivisionByLeader(dto.getTeamGm());
        }
        Division savedDivision = divisionRepository.save(Division.builder()
                .divisionName(dto.getTeamName())
                .leader(dto.getTeamGm())
                .build());
        dto.getTeamGm().setDivision(savedDivision);
        return savedDivision;
    }

    public DivisionDto createDivisionDto(String teamName, String teamGm) {
        if (teamGm.equals("0")) {
            Member master = mService.findByUsername("master");
            if (existsDivisionByLeader(master)) {
                return null;
            } else {
                teamGm = "master";
            }
        }
        DivisionDto dto = DivisionDto.builder()
                .teamName(teamName)
                .build();
        Member currentMember = mService.findByUsername(teamGm);
        dto.setTeamGm(currentMember);
        return dto;
    }

    @Transactional
    public void updateDivision(Long divisionId, String teamName, String teamGm) {
        Division currentDivision = findDivisionById(divisionId);
        currentDivision.setDivisionName(teamName);
        if (!teamGm.equals("0")) {
            Member currentMember = mService.findByUsername(teamGm);
            currentDivision.setLeader(currentMember);
            currentMember.setDivision(currentDivision);
        }

        /*if (teamGm.equals("0")) {
            if (divisionRepository.existsByLeader(mService.findByUsername("master"))) {
                divisionRepository.
            }
        }*/
    }

    public Division findDivisionByDivisionName(String divisionName) {
        return divisionRepository.findByDivisionName(divisionName);
    }

    public List<Division> findAllDivision() {
        return divisionRepository.findAll();
    }

    public Division findDivisionById(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow( () -> new DivisionNotFoundException("해당 지점을 찾을 수 없습니다."));
    }

    public Division findDivisionByLeader(Member member) {
        return divisionRepository.findByLeader(member);
    }

    public boolean existsDivisionByLeader(Member member) {
        return divisionRepository.existsByLeader(member);
    }
}
