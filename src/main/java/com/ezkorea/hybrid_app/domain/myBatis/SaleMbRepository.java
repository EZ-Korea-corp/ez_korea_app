package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface SaleMbRepository {

    List<SaleProductDto> findSaleStock(long taskId);
    List<SaleProductDto> findSaleOutFix(Map paramMap);
    List<Map<String, Object>> findStockHistory(Long stationId);
    List<SaleProductDto> findStockList(Map paramMap);
}
