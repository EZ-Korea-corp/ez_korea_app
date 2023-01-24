package com.ezkorea.hybrid_app.app.Config;

import com.ezkorea.hybrid_app.domain.account.member.MemberRepository;
import com.ezkorea.hybrid_app.domain.sale.WiperSize;
import com.ezkorea.hybrid_app.domain.sale.WiperSort;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.service.member.MemberService;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {
    private final MemberRepository memberRepository;
    private final WiperRepository wiperRepository;
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
    public void testWiperDataInit() {
        if (wiperRepository.findAll().size() == 0) {
            for (WiperSize size : WiperSize.values()) {
                for (WiperSort sort : WiperSort.values()) {
                    if (!size.getName().equals("700")) {
                        if (!sort.getName().equals("size700")) {
                            Wiper w = Wiper.builder()
                                    .wiperSize(size.getName())
                                    .wiperSort(sort.getName())
                                    .wiperPrice(sort.getPrice())
                                    .build();
                            wiperRepository.save(w);
                        }
                    }
                    if (size.getName().equals("700")) {
                        if (sort.getName().equals("size700")) {
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
