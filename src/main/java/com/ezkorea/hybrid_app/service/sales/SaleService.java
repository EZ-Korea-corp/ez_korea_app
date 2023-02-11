package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.*;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final GasStationRepository gsRepository;
    private final DailyTaskRepository dtRepository;

    private final StockRepository stockRepository;
    private final WiperRepository wpRepository;

    private final SaleMbRepository saleMbRepository;
    private final WiperService wiperService;
    private final GasStationService gsService;

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
                .status(dto.getStatus())
                .payment(dto.getPayment())
                .count(1)
                .wiper(currentWiper)
                .build();
        spRepository.save(newSaleProduct);
    }

    @Transactional
    public void saveDailyGasStation(String stationName, Member member) {
        DailyTask task = findByMemberAndDate(member);
        task.setGasStation(gsService.findByStationName(stationName));
    }

    @Transactional
    public void saveInputProduct(Member member, List<SaleProductDto> data) {
        DailyTask currentTask = findByMemberAndDate(member);
        spRepository.deleteByTaskAndStatus(currentTask, SaleStatus.IN.toString()); // 재등록

        data.forEach(item -> {
            // 입력된 입고만 등록
            if(item.getCount() > 0) {
                SaleProduct newInputProduct = SaleProduct.builder()
                        .task(currentTask)
                        .status(SaleStatus.IN.toString())
                        .count(item.getCount())
                        .wiper(wpRepository.findById(item.getWiper()).get())
                        .build();

                spRepository.save(newInputProduct);
            }
        });
    }

    @Transactional
    public List<SaleProduct> findInputProduct(Member member) {
        DailyTask currentTask = findByMemberAndDate(member);
        List<SaleProduct> inputList = spRepository.findAllByTaskAndStatus(currentTask, SaleStatus.IN.toString());

        return inputList;
    }

    public Map<String, Object> findCurrentTask(Member member) {
        Map<String, Object> map = new HashMap<>();
        DailyTask currentTask = findByMemberAndDate(member);

        if (currentTask.getGasStation() != null) {
            //총계
            List<SaleProduct> inputList = currentTask.getProductList()
                                                     .stream()
                                                     .filter( p -> p.getStatus().equals(SaleStatus.OUT.toString()))
                                                     .toList();
            //카드
            ArrayList<SaleProduct> cardList = inputList.stream()
                    .filter(product -> product.getPayment().equals(Payment.CARD.getViewName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            //현금
            ArrayList<SaleProduct> cashList = inputList.stream()
                    .filter(product -> product.getPayment().equals(Payment.CASH.getViewName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            map.put("name", currentTask.getMember().getName());
            map.put("stationNm", currentTask.getGasStation().getStationName());
            map.put("location", currentTask.getGasStation().getStationLocation());
            map.put("count", inputList.size());
            map.put("list", inputList);
            map.put("cardList", cardList);
            map.put("cashList", cashList);
        }

        return map;
    }

    public List<SaleProductDto> findSaleStat(Member member, Map<String, Object> paramMap) {
        List<SaleProductDto> statList = new ArrayList<>();
        DailyTask currentTask = findByMemberAndDate(member);
        paramMap.put("taskId", currentTask.getId());

        if(SaleStatus.STOCK.toString().equals(paramMap.get("status"))) {
            statList = saleMbRepository.findSaleStock(currentTask.getId());
        } else {
            statList = saleMbRepository.findSaleOutFix(paramMap);
        }

        return statList;
    }

    @Transactional
    public void closeTask(Member member) {
        DailyTask currentTask = findByMemberAndDate(member);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", SaleStatus.STOCK.toString());

        // 재고 현황
        List<SaleProductDto> stockList = findSaleStat(member, paramMap);

        // 재등록
        stockRepository.deleteByGasStationAndDate(currentTask.getGasStation(), LocalDate.now());

        stockList.forEach(item -> {
            Stock stock = Stock.builder()
                    .date(LocalDate.now())
                    .member(member)
                    .gasStation(currentTask.getGasStation())
                    .wiper(wpRepository.findById(item.getWiper()).get())
                    .count(item.getCount())
                    .build();

            stockRepository.save(stock);
        });
    }

    public List<Map<String, Object>> findStockHistory(Long id) {
        return saleMbRepository.findStockHistory(id);
    }

    public List<SaleProductDto> findStockList(Map<String, Object> paramMap) {
        return saleMbRepository.findStockList(paramMap);
    }

    public void deleteSale(Long id) {
        spRepository.deleteById(id);
    }
}
