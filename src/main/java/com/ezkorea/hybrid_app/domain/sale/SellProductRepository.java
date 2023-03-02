package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.timetable.SellProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SellProductRepository extends JpaRepository<SellProduct, Long> {

}