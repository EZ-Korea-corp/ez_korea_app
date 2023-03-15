package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface SaleMbRepository {
    /**
     * 명예의전당 기간별 갯수
     * @Param paramMap pStatus(일,주,월간), date(검색일자)
     * */
    List<Map<String, Object>> findStatList(Map paramMap);
    /**
     * 전체통계 기간별 매출
     * @Param paramMap pStatus(일,주,월간), date(검색일자)
     * */
    List<Map<String, Object>> findTotalStat(Map paramMap);

    /**
     * 결제수단별 통계
     * @Param paramMap tTid(특정업무), pStatus(일,주,월간), date(검색일자)
     * */
    List<Map<String, Object>> findTablePrice(Map paramMap);

    /**
     * 와이퍼 종류별 통계
     * @Param paramMap pStatus(일,주,월간), date(검색일자)
     * */
    List<Map<String, Object>> findTableCount(Map paramMap);

    /**
     * 불량목록
     * @Param paramMap tTid(특정업무)
     * */
    List<Map<String, Object>> findTableFix(Map paramMap);

    /**
     * 주유소-일자별 판매목록
     * @Param paramMap id(주유소), date(검색일자)
     * */
    List<Map<String, Object>>findSellDetailByStationAndDate(Map paramMap);

    /**
     * 일자별 모든 판매목록
     * @Param paramMap date(검색일자)
     * */
    List<Map<String, Object>>findDayStatList(Map paramMap);
}
