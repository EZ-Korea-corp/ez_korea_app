package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface SaleMbRepository {

    List<Map<String, Object>> findStatList(Map paramMap);
    List<Map<String, Object>> findTotalStat(Map paramMap);

    List<Map<String, Object>> findTablePrice(Map paramMap);
    List<Map<String, Object>> findTableCount(Map paramMap);
    List<Map<String, Object>> findTableFix(Map paramMap);


    /**
     * 주유소-일자별 판매목록
     * @Param paramMap id(주유소), date(검색일자)
     * */
    List<Map<String, Object>>findSellDetailByStationAndDate(Map paramMap);
}
