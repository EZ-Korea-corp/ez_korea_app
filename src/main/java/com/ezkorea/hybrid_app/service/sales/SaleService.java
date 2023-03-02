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

    /**
     * TimeTable 저장
     * */
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

    /**
     * TimeTable 조회
     * @param tableId TimeTable의 id
     * */
    public TimeTable findTableById(Long tableId) {
        return ttRepository.findById(tableId)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
    }

    /**
     * 특정일자, 회원의 판매,고장(입고제외) List<TimeTable> 조회
     * */
    public List<TimeTable> findTableList(LocalDate date, Member member) {
        return ttRepository.findAllByTaskDateAndMemberAndPartNot(date, member, PartTime.IN.getKey());
    }

    /**
     * 특정일자의 모든입고 List<TimeTable> 조회
     * */
    public List<TimeTable> findInputTableList(LocalDate date) {
        return ttRepository.findAllByTaskDateAndPart(date, PartTime.IN.getKey());
    }

    /**
     * 특정일자 입고의 List<TimeTable>를 뷰용 Map으로 매핑조회
     * */
    public List<Map<String, Object>> findInputTableList(Map<String, String> paramMap) {
        List<Map<String, Object>> returnList = new ArrayList<>();
        List<TimeTable> list = findInputTableList(LocalDate.parse(paramMap.get("date"), DateTimeFormatter.ISO_DATE));

        // 입고 테이블 리스트
        list.forEach(item -> {
            Map<String , Object> map = new HashMap<>();
            map.put("tTid", item.getId());
            map.put("name", item.getGasStation().getStationName());
            map.put("memo", item.getMemo());

            returnList.add(map);
        });

        return returnList;
    }

    /**
     * 특정일자 입고의 상세조회
     * */
    public Map<String, Object> findInputList(Map<String, String> paramMap) {
        Map<String, Object> returnMap = new HashMap<>();
        TimeTable table = findTableById(Long.valueOf(paramMap.get("tTid")));
        
        // 입고목록
        List<SaleProductDto> saleDtoList = new ArrayList<>();
        table.getSaleList().forEach(item ->{
            SaleProductDto saleDto = new SaleProductDto();
            saleDto.setWiper(item.getWiper().getId());
            saleDto.setCount(item.getCount());

            saleDtoList.add(saleDto);
        });
        
        returnMap.put("id", table.getId());
        returnMap.put("name", table.getGasStation().getStationName());
        returnMap.put("memo", table.getMemo());
        returnMap.put("list", saleDtoList);
        
        return returnMap;
    }

    /**
     * 특정일자 상태의 전체 List<TimeTable> 조회
     * */
    public List<TimeTable> findAllTableListByStt(Long tTid) {
        TimeTable timeTable = findTableById(tTid);
        return ttRepository.findAllByTaskDateAndGasStationAndPart(timeTable.getTaskDate(), timeTable.getGasStation(), timeTable.getPart());
    }

    /**
     * 판매, 고장 등록
     * */
    @Transactional
    public void saveSellProduct(TimeTableDto timeTableDto) {
        TimeTable timeTable = findTableById(timeTableDto.getId());

            // 판매, 고장 등록
            timeTableDto.getSellDtoList().forEach(item -> {
                if(item.getStatus().equals(SaleStatus.OUT.toString())) {
                    // 기존 리스트
                    SellProduct sellProduct = timeTable.getSellList()
                            .stream()
                            .filter(p -> p.getStatus().equals(SaleStatus.OUT.toString()) && p.getSort().equals(item.getSort()) && p.getPayment().equals(item.getPayment()))
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

    /**
     * 재고 등록
     * */
    @Transactional
    public void saveStockProduct(TimeTableDto timeTableDto) {
        TimeTable timeTable = findTableById(timeTableDto.getId());
        
        // 삭제후 재등록
        List<TimeTable> list = findAllTableListByStt(timeTableDto.getId());
        list.forEach(item -> {
            spRepository.deleteByTimeTable(item);
        });

        timeTableDto.getSaleDtoList().forEach(item -> {
            // 입력된 재고만 등록
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

    /**
     * 입고 등록
     * */
    public void saveInputProduct(TimeTableDto timeTableDto, Member member) {
        // timeTable 생성
        GasStation station = gsService.findStationById(timeTableDto.getStationId());
        TimeTable newTimeTable = TimeTable.builder()
                .gasStation(station)
                .member(member)
                .taskDate(LocalDate.now())
                .part(PartTime.IN.getKey())
                .memo(timeTableDto.getMemo())
                .build();

        ttRepository.save(newTimeTable);

        // 입고 등록
        saveSaleProduct(newTimeTable, timeTableDto);
    }

    /**
     * 입고 수정
     * */
    @Transactional
    public void updateInputProduct(TimeTableDto timeTableDto) {
        TimeTable table = findTableById(timeTableDto.getId());
        table.setGasStation(gsService.findStationById(timeTableDto.getStationId()));
        table.setMemo(timeTableDto.getMemo());

        // 입고 재등록
        spRepository.deleteByTimeTable(table);
        saveSaleProduct(table, timeTableDto);
    }

    /**
     * 입고 와이퍼 등록
     * */
    private void saveSaleProduct(TimeTable talble, TimeTableDto dto) {
        // 입력된 입고만 등록
        dto.getSaleDtoList().forEach(item -> {

            if(item.getCount() > 0) {
                SaleProduct newProduct = SaleProduct.builder()
                        .timeTable(talble)
                        .status(item.getStatus())
                        .count(item.getCount())
                        .wiper(wpRepository.findById(item.getWiper()).get())
                        .build();

                spRepository.save(newProduct);
            }
        });

    }

    /**
     * 재고리스트 조회
     * */
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

    /**
     * 판매현황 조회
     * */
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

    /**
     * 판매현황 조회
     * */
    @Transactional
    public List<SaleProduct> findInputProduct(Member member, Map<String, Long> data) {
        DailyTask currentTask = findByMemberAndStation(member, data.get("stationId"));
        List<SaleProduct> inputList = spRepository.findAllByTaskAndStatusAndRn(currentTask, SaleStatus.IN.toString(), data.get("rn").intValue());

        return inputList;
    }

    /**
     * 판매 INDEX페이지 조회
     * */
    public Map<String, Object> findTimeTable(Long tableId) {
        Map<String, Object> returnMap = new HashMap<>();
        TimeTable timeTable = findTableById(tableId);

        // 판매, 고장리스트
        List<Map<String, Object>> sellList = new ArrayList<>();
        List<Map<String, Object>> fixList  = new ArrayList<>();
        int count = 0;
        int cash = 0;
        int card = 0;
        for (SellProduct sellProduct : timeTable.getSellList()) {
            for (WiperSort sort : WiperSort.values()) {
                if(sort.getName().toLowerCase().equals(sellProduct.getSort())) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", sellProduct.getId());
                    map.put("name", sort.getViewName());

                    if(SaleStatus.FIX.toString().equals(sellProduct.getStatus())) {
                        //고장 목록
                        map.put("memo", sellProduct.getMemo());
                        fixList.add(map);

                    } else {
                        // 판매 목록
                        String payment = "";
                        int _count = sellProduct.getCount();
                        count += _count;

                        // 판매수단별 금액
                        if(Payment.CARD.toString().equals(sellProduct.getPayment())) {
                            card += _count * sort.getPrice();
                            payment = Payment.CARD.getViewName();
                        } else {
                            cash += _count * sort.getPrice();
                            payment = Payment.CASH.getViewName();
                        }

                        map.put("payment", payment);
                        map.put("count", _count);
                        sellList.add(map);
                    }
                }
            }
        }

        // 총계(판매)
        Map<String, Object> sellMap = new HashMap<>();
        sellMap.put("name", "총계");
        sellMap.put("payment", "");
        sellMap.put("count", count);
        sellList.add(sellMap);

        // 기타
        returnMap.put("sellList", sellList);
        returnMap.put("fixList" , fixList);
        returnMap.put("card", card);
        returnMap.put("cash", cash);
        returnMap.put("table", timeTable);

        return returnMap;
    }

    /**
     * TimeTable삭제
     * @param tTid TimeTable의 id
     * */
    public void deleteInputTable(Long tTid) {
        ttRepository.deleteById(tTid);
    }

    public DailyTask findByMemberAndStation(Member member, Long stationId) {
        GasStation station = gsService.findStationById(stationId);

        return dtRepository.findByTaskDateAndMemberAndGasStation(LocalDate.now(), member, station)
                .orElseThrow( () -> new MemberNotFoundException("해당 유저는 오늘 출근하지 않았습니다."));
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
