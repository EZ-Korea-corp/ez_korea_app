package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface saleMbRepository {

    List<SaleProductDto> findSaleStock(long taskId);
    List<SaleProductDto> selectSaleOutFix(Map paramMap);
}
