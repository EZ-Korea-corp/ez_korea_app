package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.domain.user.division.Position;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionService {
    private final ModelMapper mapper;
    private final DivisionRepository divisionRepository;
    private final MemberService memberService;
    public Division saveNewDivision(Team team, Member member, DivisionDto dto) {

        Division division = mapper.map(dto, Division.class);
        division.addBasicInfo(team, member);

        return divisionRepository.save(division);
    }

    public Division findByMember(Member member) {
        return divisionRepository.findByMember(member)
                .orElseThrow( () -> new MemberNotFoundException("해당 멤버의 소속을 찾을 수 없습니다."));
    }

    public DivisionDto makeNewDivisionDto(String status, Position position) {
        DivisionDto newDto = new DivisionDto();
        newDto.setPosition(position);
        newDto.setStatus(status);
        return newDto;
    }

    @Transactional
    public void setEmployeeDivision(String username, Member leader) {
        Member employee = memberService.findByUsername(username);
        Division leaderDivision = findByMember(leader);
        DivisionDto dto = makeNewDivisionDto("승인", Position.MEMBER);
        Division division = saveNewDivision(
                leaderDivision.getTeam(), employee, dto
        );

        employee.setDivision(division);
    }
}
