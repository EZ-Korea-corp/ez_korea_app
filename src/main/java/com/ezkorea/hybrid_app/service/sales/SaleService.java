package com.ezkorea.hybrid_app.service.sales;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.gas.GasStationRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.sale.*;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.task.DailyTaskRepository;
import com.ezkorea.hybrid_app.domain.timetable.PartTime;
import com.ezkorea.hybrid_app.domain.timetable.SellProduct;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import com.ezkorea.hybrid_app.domain.timetable.TimeTableRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.domain.wiper.WiperRepository;
import com.ezkorea.hybrid_app.domain.wiper.WiperSort;
import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import com.ezkorea.hybrid_app.web.dto.TaskDto;
import com.ezkorea.hybrid_app.web.dto.TimeTableDto;
import com.ezkorea.hybrid_app.web.dto.WiperDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import com.ezkorea.hybrid_app.web.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Time;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SaleService {

    private final ModelMapper modelMapper;
    private final SaleProductRepository spRepository;
    private final SellProductRepository sellRepository;
    private final GasStationRepository gsRepository;
    private final DailyTaskRepository dtRepository;
    private final TimeTableRepository ttRepository;
    private final WiperRepository wpRepository;
    private final SaleMbRepository saleMbRepository;

    private final WiperService wiperService;
    private final GasStationService gsService;

    @Transactional
    public Long saveTimeTable(Map<String, Object> pramMap, Member member) {
        String part = (String)pramMap.get("part");
        GasStation station = gsService.findStationById(Long.valueOf(String.valueOf(pramMap.get("stationId"))));
        TimeTable table = ttRepository.findByTaskDateAndPartAndGasStationAndMember(LocalDate.now(), part, station, member);

        if(table == null) {
            TimeTable newTimeTable = TimeTable.builder()
                    .gasStation(station)
                    .member(member)
                    .taskDate(LocalDate.now())
                    .part(part)
                    .build();

            ttRepository.save(newTimeTable);
            return newTimeTable.getId();
        }

        return 0L;
    }

    public List<TimeTable> findTableList(LocalDate date, Member member) {
        return ttRepository.findAllByTaskDateAndMemberAndPartNot(date, member, PartTime.IN.getKey());
    }


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

    public TimeTable findTableById(Long tableId) {
        return ttRepository.findById(tableId)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
    }

    public List<TimeTable> findAllTableListByStt(Long tTid) {
        TimeTable timeTable = findTableById(tTid);
        return ttRepository.findAllByTaskDateAndGasStationAndPart(timeTable.getTaskDate(), timeTable.getGasStation(), timeTable.getPart());
    }

    @Transactional
    public void saveSellProduct(TimeTableDto timeTableDto) {
        TimeTable timeTable = findTableById(timeTableDto.getId());

            // 판매, 고장 등록
            timeTableDto.getSellDtoList().forEach(item -> {
                if(item.getStatus().equals(SaleStatus.OUT.toString())) {
                    // 기존 리스트
                    SellProduct sellProduct = timeTable.getSellList()
                            .stream()
                            .filter(p -> p.getStatus().equals(SaleStatus.OUT.toString()) && p.getSort().equals(item.getSort()))
                            .findFirst()
                            .orElse(null);

                    if(sellProduct != null) {
                        sellProduct.setCount(sellProduct.getCount() + item.getCount());
                    } else {
                        sellProduct = SellProduct.builder()
                                .timeTable(timeTable)
                                .sort(item.getSort())
                                .payment(item.getPayment())
                                .status(SaleStatus.OUT.getViewName())
                                .count(item.getCount())
                                .build();

                        sellRepository.save(sellProduct);
                    }
                } else {
                    SellProduct fixProduct = SellProduct.builder()
                            .timeTable(timeTable)
                            .sort(item.getSort())
                            .payment(item.getPayment())
                            .memo(item.getMemo())
                            .status(SaleStatus.FIX.getViewName())
                            .count(item.getCount())
                            .build();

                    sellRepository.save(fixProduct);
                }
            });

    }

    @Transactional
    public void saveInputProduct(TimeTableDto timeTableDto) {
        TimeTable timeTable = findTableById(timeTableDto.getId());
        
        // 삭제후 재등록
        List<TimeTable> list = findAllTableListByStt(timeTableDto.getId());
        list.forEach(item -> {
            spRepository.deleteByTimeTable(item);
        });

        timeTableDto.getSaleDtoList().forEach(item -> {
            // 입력된 판매만 등록
            if(item.getCount() > 0) {
                SaleProduct newProduct = SaleProduct.builder()
                        .timeTable(timeTable)
                        .status(item.getStatus())
                        .count(item.getCount())
                        .wiper(wpRepository.findById(item.getWiper()).get())
                        .build();

                spRepository.save(newProduct);
            }
        });
    }


    public List<SaleProductDto> findStockProduct(Map<String, String> paramMap) {
        List<TimeTable> tableList = findAllTableListByStt(Long.valueOf(paramMap.get("tTid")));
        
        // 재고리스트
        List<SaleProductDto> list = new ArrayList<>();
        tableList.forEach(item -> {
            item.getSaleList().forEach(_item -> {
                if(_item.getStatus().equals(SaleStatus.STOCK.toString()) && _item.getCount() > 0) {
                    SaleProductDto dto = new SaleProductDto();
                    dto.setWiper(_item.getWiper().getId());
                    dto.setCount(_item.getCount());

                    list.add(dto);
                }
            });
        });

        return list;
    }

    @Transactional
    public List<SaleProduct> findInputProduct(Member member, Map<String, Long> data) {
        DailyTask currentTask = findByMemberAndStation(member, data.get("stationId"));
        List<SaleProduct> inputList = spRepository.findAllByTaskAndStatusAndRn(currentTask, SaleStatus.IN.toString(), data.get("rn").intValue());

        return inputList;
    }

    public Map<String, Object> findTimeTable(Long tableId) {
        Map<String, Object> returnMap = new HashMap<>();
        TimeTable timeTable = findTableById(tableId);

        // 종류별 판매갯수, 결제수단별 갯수
        List<Map<String, Object>> sellList = new ArrayList<>();
        List<Map<String, Object>> fixList  = new ArrayList<>();
        int selTotalCount = 0;
        int card = 0;
        int cash = 0;
        for (WiperSort sort : WiperSort.values()) {
            int selCount = 0;

            for (SellProduct sellProduct : timeTable.getSellList()) {
                if(sort.getName().toLowerCase().equals(sellProduct.getSort())) {
                    if(SaleStatus.FIX.toString().equals(sellProduct.getStatus())) {
                        // 고장
                        Map<String, Object> fixMap = new HashMap<>();
                        fixMap.put("id", sellProduct.getId());
                        fixMap.put("name", sort.getInit());
                        fixMap.put("count", sellProduct.getCount());
                        fixMap.put("memo", sellProduct.getMemo());
                        fixList.add(fixMap);
                    } else {
                        selCount += sellProduct.getCount();

                        if(Payment.CARD.toString().equals(sellProduct.getPayment())) {
                            card += selCount * sort.getPrice();
                        } else {
                            cash += selCount * sort.getPrice();
                        }

                        // 판매
                        Map<String, Object> sellMap = new HashMap<>();
                        sellMap.put("id", sellProduct.getId());
                        sellMap.put("name", sort.getInit());
                        sellMap.put("count", selCount);
                        sellList.add(sellMap);
                    }
                }
            }

            selTotalCount += selCount;
        }

        // 총계(판매)
        Map<String, Object> sellMap = new HashMap<>();
        sellMap.put("name" , "총계");
        sellMap.put("count", selTotalCount);
        sellList.add(sellMap);

        // 기타
        returnMap.put("sellList", sellList);
        returnMap.put("fixList" , fixList);
        returnMap.put("card", card);
        returnMap.put("cash", cash);
        returnMap.put("table", timeTable);

        return returnMap;
    }

    public Map<String, Object> findTableStat(Map<String, String> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        List<Map<String, Object>> priceList = saleMbRepository.findTablePrice(paramMap);
        List<Map<String, Object>> countList = saleMbRepository.findTableCount(paramMap);
        List<Map<String, Object>> fixList   = saleMbRepository.findTableFix(paramMap);

        // 결제수단명 추가
        priceList.forEach(item -> {
            item.put("NAME", Payment.of((String)item.get("PAYMENT")));
        });

        // 결제수단명 추가
        fixList.forEach(item -> {
            item.put("NAME", WiperSort.of((String)item.get("SORT")));
        });
                
        returnMap.put("priceList", priceList);
        returnMap.put("countList", countList);
        returnMap.put("fixList"  , fixList);

        

        return returnMap;
    }

    public  List<SaleProductDto> findStockHistory(Map<String, Object> paramMap) {
        return saleMbRepository.findStockHistory(paramMap);
    }

    public void deleteSale(Long id) {
        sellRepository.deleteById(id);
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
/*        List<SaleProductDto> saleStat = findSaleStat(member, paramMap);
        saleStat.forEach(item -> {
            SaleProduct withdrawProduct = SaleProduct.builder()
                    .task(currentTask)
                    .status(SaleStatus.END.toString())
                    .count(item.getCount())
                    .wiper(wpRepository.findById(item.getWiper()).get())
                    .build();

            spRepository.save(withdrawProduct);
        });*/
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

    public List<Map<String, Object>> findInProductList(Member member, Long id) {
        DailyTask currentTask = findByMemberAndStation(member, id);
        return saleMbRepository.findInProductList(currentTask.getId());
    }

    public List<Map<String, Object>> findInOutProductList(Long id) {
        return saleMbRepository.findInProductList(id);
    }
}
