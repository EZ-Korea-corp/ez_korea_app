package com.ezkorea.hybrid_app.app.Config;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.Position;
import com.ezkorea.hybrid_app.domain.user.division.Status;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.wiper.WiperSize;
import com.ezkorea.hybrid_app.domain.wiper.WiperSort;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WiperRepository wiperRepository;
    private final GasStationRepository gasStationRepository;
    private final MemberService memberService;
    private final TeamService teamService;
    private final DivisionService divisionService;

    @PostConstruct
    public void testMemberDataInit() {
        if (!memberRepository.existsByUsername("01011111111")) {
            Member member = memberService.saveNewMember(makeNewMember("01011111111", "고봉민", Role.LEADER));
            Team newTeam = teamService.saveNewTeam(member.getName() + "팀");
            Division division = divisionService.saveNewDivision(newTeam, member, divisionService.makeNewDivisionDto(Status.COMPLETE, Position.LEADER));
            member.setDivision(division);
            memberRepository.save(member);
        }
        if (!memberRepository.existsByUsername("01011111111")) {
            memberService.saveNewMember(makeNewMember("01022222222", "김경자", Role.EMPLOYEE));
        }
        if (!memberRepository.existsByUsername("01011111111")) {
            memberService.saveNewMember(makeNewMember("01033333333", "한문철", Role.EMPLOYEE));
        }
        StringBuilder sb;
        for (int i = 0; i < 15; i++) {
            Random random = new Random();
            sb = new StringBuilder();
            for (int j = 0; j < 11; j++) {
                int createNum = random.nextInt(9);
                sb.append(createNum);
            }
            if (!memberRepository.existsByUsername(sb.toString())) {
                memberService.saveNewMember(makeNewMember(sb.toString(), sb.substring(0, 3), Role.EMPLOYEE));
            }
        }
    }

    public SignUpDto makeNewMember(String username, String name, Role role) {
        SignUpDto newDto = new SignUpDto();
        newDto.setUsername(username);
        newDto.setPassword("1234");
        newDto.setName(name);
        newDto.setSex("MALE");
        newDto.setRole(role);

        return newDto;
    }

    @PostConstruct
    public void testGasStationDataInit() {
        if (gasStationRepository.findAll().size() == 0) {
            GasStation gs = GasStation.builder()
                    .stationName("배트맨 주유소")
                    .stationLocation("경기도 수원시 장안구 랄로 1234번길 56")
                    .build();
            gasStationRepository.save(gs);

            gs = GasStation.builder()
                    .stationName("한문철 주유소")
                    .stationLocation("경기도 용인시 수지구 랄로 1004번길 44")
                    .build();
            gasStationRepository.save(gs);
        }
    }

    @PostConstruct
    public void testWiperDataInit() {
        if (wiperRepository.findAll().size() == 0) {
            for (WiperSort sort : WiperSort.values()) {
                for (WiperSize size : WiperSize.values()) {
                    if (!size.getName().equals("700")) {
                        if (!sort.getName().equals("size_700")) {
                            Wiper w = Wiper.builder()
                                    .wiperSize(size.getName())
                                    .wiperSort(sort.getName())
                                    .wiperPrice(sort.getPrice())
                                    .wiperViewName(sort.getViewName())
                                    .build();
                            wiperRepository.save(w);
                        }
                    }
                    if (size.getName().equals("700")) {
                        if (sort.getName().equals("size_700")) {
                            Wiper w = Wiper.builder()
                                    .wiperSize(size.getName())
                                    .wiperSort(sort.getName())
                                    .wiperPrice(sort.getPrice())
                                    .wiperViewName(sort.getViewName())
                                    .build();
                            wiperRepository.save(w);
                        }
                    }
                }
            }
        }
    }
}
