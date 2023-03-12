package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
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
    private final TeamService tService;

    @Transactional
    public Division saveNewDivision(DivisionDto dto) {
        return divisionRepository.save(Division.builder()
                .divisionName(dto.getTeamName())
                .leader(dto.getTeamGm())
                .build());
    }

    public DivisionDto createDivisionDto(String teamName, String teamGm) {
        DivisionDto dto = DivisionDto.builder()
                .teamName(teamName)
                .build();
        if (mService.existsMemberByUsername(teamGm)) {
            Member currentMember = mService.findByUsername(teamGm);
            dto.setTeamGm(currentMember);
        } else {
            dto.setTeamGm(null);
        }
        return dto;
    }

    @Transactional
    public void updateDivision(Long divisionId, String teamName, String teamGm) {
        Division currentDivision = findDivisionById(divisionId);
        if (mService.existsMemberByUsername(teamGm)) {
            Member currentMember = mService.findByUsername(teamGm);
            currentDivision.setLeader(currentMember);
        } else {
            currentDivision.setLeader(null);
        }
        currentDivision.setDivisionName(teamName);
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

    public List<Division> findAllByLeader(Member currentMember) {
        return divisionRepository.findAllByLeader(currentMember);
    }

    @Transactional
    public void removeDivisionLeader(Member currentMember) {
        if (existsDivisionByLeader(currentMember)) {
            for (Division division : findAllByLeader(currentMember)) {
                division.setLeader(null);
            }
        }
        currentMember.setDivision(null);
    }

    @Transactional
    public void removeDivision(Long id) {
        Division currentDivision = findDivisionById(id);

        for (Team team : currentDivision.getTeamList()) {
            tService.removeTeam(team.getId());
        }

        currentDivision.setLeader(null);
        currentDivision.makeTeamListInitialization();

        divisionRepository.delete(currentDivision);
    }
}
