package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.domain.user.division.Position;
import com.ezkorea.hybrid_app.domain.user.division.Status;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionService {
    private final ModelMapper mapper;
    private final DivisionRepository divisionRepository;
    private final MemberService memberService;

    @Transactional
    public Division saveNewDivision(Team team, Member member, DivisionDto dto) {

        Division division = mapper.map(dto, Division.class);
        division.addBasicInfo(team, member);
        Division saveDivision = divisionRepository.save(division);
        member.setDivision(saveDivision);
        return saveDivision;
    }

    public Division findByMember(Member member) {
        return divisionRepository.findByMember(member)
                .orElseThrow( () -> new MemberNotFoundException("해당 멤버의 소속을 찾을 수 없습니다."));
    }

    public List<Division> findDivisionListByMember(Member member) {
        return divisionRepository.findAllByMember(member);
    }

    /**
     * 테스트 계정 전용 메소드
     * */
    public DivisionDto makeNewDivisionDto(Status status, Position position) {
        DivisionDto newDto = new DivisionDto();
        newDto.setPosition(position);
        newDto.setStatus(status);
        return newDto;
    }

    @Transactional
    public void setEmployeeDivision(DivisionDto dto, Member leader) {
        Member employee = memberService.findByUsername(dto.getUsername());
        Division leaderDivision = findByMember(leader);
        Division division = saveNewDivision(
                leaderDivision.getTeam(), employee, dto
        );

        employee.setDivision(division);
    }

    @Transactional(readOnly = true)
    public boolean existDivisionByMember(Member member) {
        return divisionRepository.existsByMember(member);
    }

    public List<Division> findDivisionByTeam(Team team) {
        return divisionRepository.findAllByTeam(team);
    }

    public void deleteAll(List<Division> divisionList) {
        divisionRepository.deleteAll(divisionList);
    }
}
