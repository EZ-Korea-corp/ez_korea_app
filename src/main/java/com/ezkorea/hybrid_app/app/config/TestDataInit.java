package com.ezkorea.hybrid_app.app.config;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.domain.user.member.Role;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.domain.wiper.WiperSize;
import com.ezkorea.hybrid_app.domain.wiper.WiperSort;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WiperRepository wiperRepository;
    private final GasStationRepository gasStationRepository;
    private final MemberService memberService;
    private final DivisionService divisionService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @PostConstruct
    public void memberDataInit() {
        if (!memberRepository.existsByUsername("dev")) {
            memberService.saveNewMember(makeNewMember("dev", "개발팀", Role.ROLE_MASTER, "01057868993"));
        }
        if (!memberRepository.existsByUsername("master")) {
            memberService.saveNewMember(makeNewMember("master", "남명원", Role.ROLE_MASTER, "01081815587"));
        }
    }

    @PostConstruct
    public void testMemberDataInitWhenDev() {
        if (activeProfile.equals("dev")) {
            if (!memberRepository.existsByUsername("test1")) {
                memberService.saveNewMember(makeNewMember("test1", "고봉민", Role.ROLE_EMPLOYEE, "01012341234"));
            }
            if (!memberRepository.existsByUsername("test2")) {
                memberService.saveNewMember(makeNewMember("test2", "김경자", Role.ROLE_EMPLOYEE, "01012341234"));
            }
        }
//         아래 주석 해제하면 랜덤 회원 생성 가능 사용 후 다시 주석처리!
//         makeRandomMember(10);
    }

    public void makeRandomMember(int memberCount) {

        StringBuilder sb;
        for (int i = 0; i < memberCount; i++) {
            Random random = new Random();
            sb = new StringBuilder();
            for (int j = 0; j < 11; j++) {
                int createNum = random.nextInt(9);
                sb.append(createNum);
            }
            if (!memberRepository.existsByUsername(sb.toString())) {
                memberService.saveNewMember(makeNewMember(sb.toString(), sb.substring(0, 3), Role.ROLE_EMPLOYEE, sb.toString()));
            }
        }
    }

    public SignUpDto makeNewMember(String username, String name, Role role, String phoneNumber) {
        SignUpDto newDto = new SignUpDto();
        newDto.setUsername(username);
        newDto.setMemberStatus(MemberStatus.AWAIT);
        newDto.setPassword("1234");
        newDto.setPhone(phoneNumber);
        newDto.setEmail(username + "@ezkorea.com");
        newDto.setName(name);
        newDto.setSex("MALE");
        newDto.setRole(role);

        return newDto;
    }

    @PostConstruct
    public void testGasStationDataInit() {
        if (activeProfile.equals("dev")) {
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
