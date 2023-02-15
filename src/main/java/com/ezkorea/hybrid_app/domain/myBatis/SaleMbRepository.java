package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface SaleMbRepository {

    List<SaleProductDto> findSaleStock(Map paramMap);
    List<SaleProductDto> findSaleOutFix(Map paramMap);
    List<SaleProductDto> findStockHistory(Map paramMap);
    List<SaleProductDto> findInOutDetail(Map paramMap);
    Long findLastWithdraw(Map paramMap);
    List<SaleProductDto> findLastWithdrawList(Map paramMap);
}
