package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    List<SaleProduct> findAllByTaskAndStatusAndRn(DailyTask task, String status, int rn);

    void deleteByTaskAndStatus(DailyTask task, String status);
    void deleteByTaskAndStatusAndRn(DailyTask task, String status, int rn);

    void deleteByTimeTable(TimeTable timeTable);
}