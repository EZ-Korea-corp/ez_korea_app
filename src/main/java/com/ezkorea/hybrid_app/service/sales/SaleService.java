package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.saleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.SaleProduct;
import com.ezkorea.hybrid_app.domain.sale.SaleProductRepository;
import com.ezkorea.hybrid_app.domain.sale.Stock;
import com.ezkorea.hybrid_app.domain.sale.StockRepository;
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


@Service
@RequiredArgsConstructor
public class SaleService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final GasStationRepository gsRepository;
    private final DailyTaskRepository dtRepository;

    private final StockRepository stockRepository;
    private final WiperRepository wpRepository;

    private final saleMbRepository saleMbRepository;
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
                .count(1)
                .wiper(currentWiper)
                .build();
        spRepository.save(newSaleProduct);
    }

    public List<GasStation> findAllGasStation() {
        return gsRepository.findAll();
    }

    @Transactional
    public void saveDailyGasStation(String stationName, Member member) {
        DailyTask task = findByMemberAndDate(member);
        task.setGasStation(gsService.findByStationName(stationName));
    }

    @Transactional
    public void saveInputProduct(Member member, List<SaleProductDto> data) {
        DailyTask currentTask = findByMemberAndDate(member);
        spRepository.deleteByTaskAndStatus(currentTask, "in"); // 재등록

        data.forEach(item -> {
            // 입력된 입고만 등록
            if(item.getCount() > 0) {
                SaleProduct newInputProduct = SaleProduct.builder()
                        .task(currentTask)
                        .status("in")
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
        List<SaleProduct> inputList = spRepository.findAllByTaskAndStatus(currentTask, "in");

        return inputList;
    }

    public Map<String, Object> findCurrentTask(Member member) {
        Map<String, Object> map = new HashMap<>();
        DailyTask currentTask = findByMemberAndDate(member);

        if (currentTask.getGasStation() != null) {
            List<SaleProduct> inputList = currentTask.getProductList()
                                                     .stream()
                                                     .filter( p -> p.getStatus().equals("out"))
                                                     .toList();

            map.put("name", currentTask.getMember().getName());
            map.put("stationNm", currentTask.getGasStation().getStationName());
            map.put("location", currentTask.getGasStation().getStationLocation());
            map.put("count", inputList.size());
            map.put("list", inputList);
        }

        return map;
    }

    public List<SaleProductDto> findSaleStat(Member member, Map<String, Object> paramMap) {
        List<SaleProductDto> statList = new ArrayList<>();
        DailyTask currentTask = findByMemberAndDate(member);
        paramMap.put("taskId", currentTask.getId());

        if("stock".equals(paramMap.get("status"))) {
            statList = saleMbRepository.findSaleStock(currentTask.getId());
        } else {
            statList = saleMbRepository.selectSaleOutFix(paramMap);
        }

        return statList;
    }

    @Transactional
    public void closeTask(Member member) {
        DailyTask currentTask = findByMemberAndDate(member);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", "stock");

        // 재고 현황
        List<SaleProductDto> stockList = findSaleStat(member, paramMap);

        // 재등록
        stockRepository.deleteByGasStation(currentTask.getGasStation());

        stockList.forEach(item -> {
            // 입력된 입고만 등록
            Stock stock = Stock.builder()
                    .date(LocalDate.now())
                    .gasStation(currentTask.getGasStation())
                    .wiper(wpRepository.findById(item.getWiper()).get())
                    .count(item.getCount())
                    .build();

            stockRepository.save(stock);
        });
    }
}
