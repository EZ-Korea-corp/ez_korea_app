package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.*;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.TaskDto;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final GasStationRepository gsRepository;
    private final DailyTaskRepository dtRepository;

    private final WiperRepository wpRepository;

    private final SaleMbRepository saleMbRepository;
    private final WiperService wiperService;
    private final GasStationService gsService;

    public void saveDailyTask(Member member, GasStation gasStation) {
        DailyTask dt = DailyTask.builder()
                .member(member)
                .taskDate(LocalDate.now())
                .gasStation(gasStation)
                .build();
        dtRepository.save(dt);
    }

    public DailyTask findByMemberAndStation(Member member, Long stationId) {
        GasStation station = gsService.findStationById(stationId);

        return dtRepository.findByTaskDateAndMemberAndGasStation(LocalDate.now(), member, station)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
    }

    public void saveSaleProduct(List<WiperDto> dto, Member member, Long stationId) {
        DailyTask currentTask = findByMemberAndStation(member, stationId);
        
        // 판매등록
        dto.forEach(item -> {
            Wiper currentWiper = wiperService.findWiperBySizeAndSort(item.getWiperSize(), item.getWiperSort());
            SaleProduct newSaleProduct = SaleProduct.builder()
                    .task(currentTask)
                    .status(item.getStatus())
                    .payment(item.getPayment())
                    .count(1)
                    .wiper(currentWiper)
                    .build();
            spRepository.save(newSaleProduct);
        });
    }

    @Transactional
    public boolean saveDailyGasStation(Long stationId, Member member) {
        GasStation station = gsService.findStationById(stationId);
        DailyTask currentTask = dtRepository.findByTaskDateAndMemberAndGasStation(LocalDate.now(), member, station).orElse(null);

        if(currentTask == null) {
            saveDailyTask(member, gsService.findStationById(stationId));
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void saveInputProduct(Member member, TaskDto taskDto) {
        DailyTask currentTask = findByMemberAndStation(member, taskDto.getStationId());
        spRepository.deleteByTaskAndStatus(currentTask, SaleStatus.IN.toString()); // 재등록

        taskDto.getSaleDtoList().forEach(item -> {
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
    public List<SaleProduct> findInputProduct(Member member, Long stationId) {
        DailyTask currentTask = findByMemberAndStation(member, stationId);
        List<SaleProduct> inputList = spRepository.findAllByTaskAndStatus(currentTask, SaleStatus.IN.toString());

        return inputList;
    }

    public Map<String, Object> findCurrentTask(Member member, Long stationId) {
        Map<String, Object> map = new HashMap<>();
        DailyTask currentTask = findByMemberAndStation(member, stationId);

        if (currentTask.getGasStation() != null) {
            //총계(판매, 고장)
            List<SaleProduct> detailList = currentTask.getProductList()
                    .stream()
                    .filter( p -> p.getStatus().equals(SaleStatus.OUT.toString()) || p.getStatus().equals(SaleStatus.FIX.toString()))
                    .toList();

            //판매리스트
            List<SaleProduct> inputList = currentTask.getProductList()
                                                     .stream()
                                                     .filter( p -> p.getStatus().equals(SaleStatus.OUT.toString()))
                                                     .toList();
            //판매리스트-카드
            ArrayList<SaleProduct> cardList = inputList.stream()
                    .filter(product -> product.getPayment().equals(Payment.CARD.getViewName()))
                    .collect(Collectors.toCollection(ArrayList::new));
            //판매리스트-현금
            ArrayList<SaleProduct> cashList = inputList.stream()
                    .filter(product -> product.getPayment().equals(Payment.CASH.getViewName()))
                    .collect(Collectors.toCollection(ArrayList::new));

            map.put("name", currentTask.getMember().getName());
            map.put("stationNm", currentTask.getGasStation().getStationName());
            map.put("stationId", currentTask.getGasStation().getId());
            map.put("location", currentTask.getGasStation().getStationLocation());
            map.put("count", inputList.size());
            map.put("list", detailList);
            map.put("cardList", cardList);
            map.put("cashList", cashList);
        }

        return map;
    }

    public List<SaleProductDto> findSaleStat(Member member, Map<String, Object> paramMap) {
        List<SaleProductDto> statList = new ArrayList<>();
        DailyTask currentTask = findByMemberAndStation(member, Long.valueOf(String.valueOf(paramMap.get("stationId"))));
        paramMap.put("taskId", currentTask.getId());

        if(SaleStatus.STOCK.toString().equals(paramMap.get("status"))) {
            statList = saleMbRepository.findSaleStock(paramMap);
        } else {
            statList = saleMbRepository.findSaleOutFix(paramMap);
        }

        return statList;
    }

    public  List<SaleProductDto> findStockHistory(Map<String, Object> paramMap) {
        return saleMbRepository.findStockHistory(paramMap);
    }

    public void deleteSale(Long id) {
        spRepository.deleteById(id);
    }

    public List<DailyTask> findInoutList(Long id, String date) {
        GasStation station = gsService.findStationById(id);
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return dtRepository.findAllByTaskDateAndGasStation(localDate, station);
    }

    public List<SaleProductDto> findInOutDetail(Map<String, Object> paramMap) {
        return saleMbRepository.findInOutDetail(paramMap);
    }

    public void saveWithdraw(Map<String, Object> paramMap, Member member) {
        paramMap.put("status", SaleStatus.STOCK.toString());
        DailyTask currentTask = findByMemberAndStation(member, Long.valueOf(String.valueOf(paramMap.get("stationId"))));

        // 현재 재고 > 철수
        List<SaleProductDto> saleStat = findSaleStat(member, paramMap);
        saleStat.forEach(item -> {
            SaleProduct withdrawProduct = SaleProduct.builder()
                    .task(currentTask)
                    .status(SaleStatus.END.toString())
                    .count(item.getCount())
                    .wiper(wpRepository.findById(item.getWiper()).get())
                    .build();

            spRepository.save(withdrawProduct);
        });
    }

    @Transactional
    public void deleteByTaskAndStatus(Map<String, Object> paramMap) {
        DailyTask dailyTask = lastEndTask(paramMap);

        if(dailyTask != null) {
            spRepository.deleteByTaskAndStatus(dailyTask, SaleStatus.END.toString()); // 재등록
        }
    }

    private DailyTask lastEndTask(Map<String, Object> paramMap) {
        Long lastWithdrawId = saleMbRepository.findLastWithdraw(paramMap);
        if(lastWithdrawId == null) {
            return null;
        }

        return dtRepository.findById(lastWithdrawId).orElseThrow(() -> new IdNotFoundException("데이터를 찾을 수 없습니다."));
    }

    public void findLastWithdraw(Map<String, Object> paramMap, Map<String, Object> returnMap) {
        DailyTask dailyTask = lastEndTask(paramMap);

        if(dailyTask != null) {
            paramMap.put("taskId", dailyTask.getId());
            List<SaleProductDto> withdrawList = saleMbRepository.findLastWithdrawList(paramMap);

            returnMap.put("taskDate", dailyTask.getTaskDate());
            returnMap.put("name", dailyTask.getMember().getName());
            returnMap.put("withdrawList", withdrawList);
        }
    }
}
