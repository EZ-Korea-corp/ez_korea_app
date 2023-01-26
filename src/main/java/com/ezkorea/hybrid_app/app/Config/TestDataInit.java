package com.ezkorea.hybrid_app.app.Config;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.wiper.WiperSize;
import com.ezkorea.hybrid_app.domain.wiper.WiperSort;
import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WiperRepository wiperRepository;
    private final GasStationRepository gasStationRepository;
    private final MemberService memberService;

    @PostConstruct
    public void testMemberDataInit() {
        if (!memberRepository.existsByUsername("test1")) {
            memberService.saveNewMember(makeNewMember("test1", "고봉민", true));
        }
        if (!memberRepository.existsByUsername("test2")) {
            memberService.saveNewMember(makeNewMember("test2", "김경자", false));
        }
        if (!memberRepository.existsByUsername("test3")) {
            memberService.saveNewMember(makeNewMember("test3", "한문철", false));
        }
    }

    public SignUpDto makeNewMember(String username, String name, boolean isLeader) {
        SignUpDto newDto = new SignUpDto();
        newDto.setUsername(username);
        newDto.setPassword("1234");
        newDto.setName(name);
        newDto.setSex("MALE");
        newDto.setLeader(isLeader);

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
