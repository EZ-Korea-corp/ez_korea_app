package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.web.dto.SaleProductDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommuteMbRepository {

    List<Map<String, String>> findCommuteTime(String date);

    List<Map<String, String>> findTaskDateList(Map map);

    List<Map<String, String>> findMemberChart();
}
