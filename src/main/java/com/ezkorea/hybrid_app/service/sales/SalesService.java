package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.sale.SaleProductRepository;
import com.ezkorea.hybrid_app.web.dto.SellDto;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final DailyTaskRepository dtRepository;

    public void saveDailyTask(Member member) {
        DailyTask dt = DailyTask.builder()
                .member(member)
                .taskDate(LocalDate.now())
                .build();
        dtRepository.save(dt);
    }

    @Transactional
    public void saveTaskProduct(SaleProduct product) {
        findByMemberAndDate(product.getSeller()).addProduct(product);
    }

    public void saveSaleProduct(SellDto sellDto, Member member) {
        SaleProduct newSaleProduct = modelMapper.map(sellDto, SaleProduct.class);
        newSaleProduct.setBasicInfo(member, findByMemberAndDate(member));
        spRepository.save(newSaleProduct);
        saveTaskProduct(newSaleProduct);
    }

    public DailyTask findByMemberAndDate(Member member) {
        return dtRepository.findByTaskDateAndMember(LocalDate.now(), member)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
    }
}
