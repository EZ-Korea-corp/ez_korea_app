package com.ezkorea.hybrid_app.domain.myBatis;

import com.ezkorea.hybrid_app.domain.adjustment.Adjustment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdjustmentMbRepository {

    List<Map<String, String>> findAdjStat(Map map);

}
