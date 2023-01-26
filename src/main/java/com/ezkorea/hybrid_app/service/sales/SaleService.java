package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.sale.SaleProductRepository;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final DailyTaskRepository dtRepository;

    private final WiperService wiperService;

    public void saveDailyTask(Member member) {
        DailyTask dt = DailyTask.builder()
                .member(member)
                .taskDate(LocalDate.now())
                .gasStation(null)
                .build();
        dtRepository.save(dt);
    }

    public DailyTask findByMemberAndDate(Member member) {
        return dtRepository.findByTaskDateAndMember(LocalDate.now(), member)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
    }

    public void saveSaleProduct(WiperDto dto, Member member) {
        Wiper currentWiper = wiperService.findWiperBySizeAndSort(dto.getWiperSize(), dto.getWiperSort());
        SaleProduct newSaleProduct = SaleProduct.builder()
                .task(findByMemberAndDate(member))
                .status("out")
                .count(1)
                .wiper(currentWiper)
                .build();
        spRepository.save(newSaleProduct);
//        saveTaskProduct();
    }

}
