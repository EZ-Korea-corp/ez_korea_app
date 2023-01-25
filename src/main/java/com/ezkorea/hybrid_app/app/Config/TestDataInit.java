package com.ezkorea.hybrid_app.app.Config;

import com.ezkorea.hybrid_app.domain.user.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.sale.WiperSize;
import com.ezkorea.hybrid_app.domain.sale.WiperSort;
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
        if (!memberRepository.existsByUsername("01012341234")) {
            memberService.saveNewMember(makeNewMember("01012341234", "김기만"));
        }
        if (!memberRepository.existsByUsername("01011112222")) {
            memberService.saveNewMember(makeNewMember("01011112222", "남궁성"));
        }
    }

    @PostConstruct
    public void testGasStationDataInit() {
        if (gasStationRepository.findAll().size() == 0) {
            GasStation gs = GasStation.builder()
                    .stationName("테스트 주유소1")
                    .stationLocation("경기도 수원시 장안구")
                    .build();
            gasStationRepository.save(gs);

            gs = GasStation.builder()
                    .stationName("테스트 주유소2")
                    .stationLocation("경기도 용인시 수지구")
                    .build();
            gasStationRepository.save(gs);
        }
    }

    @PostConstruct
    public void testWiperDataInit() {
        if (wiperRepository.findAll().size() == 0) {
            for (WiperSize size : WiperSize.values()) {
                for (WiperSort sort : WiperSort.values()) {
                    if (!size.getName().equals("700")) {
                        if (!sort.getName().equals("size_700")) {
                            Wiper w = Wiper.builder()
                                    .wiperSize(size.getName())
                                    .wiperSort(sort.getName())
                                    .wiperPrice(sort.getPrice())
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
                                    .build();
                            wiperRepository.save(w);
                        }
                    }
                }
            }
        }
    }

    public SignUpDto makeNewMember(String username, String name) {
        SignUpDto newDto = new SignUpDto();
        newDto.setUsername(username);
        newDto.setPassword("1234");
        newDto.setName(name);
        newDto.setSex("MALE");

        return newDto;
    }
}
