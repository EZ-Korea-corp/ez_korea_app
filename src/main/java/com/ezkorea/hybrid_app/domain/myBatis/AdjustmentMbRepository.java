package com.ezkorea.hybrid_app.domain.myBatis;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdjustmentMbRepository {

    void updateAdjustment(Map map);
}
