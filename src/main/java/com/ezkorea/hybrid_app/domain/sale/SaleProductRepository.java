package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.timetable.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    void deleteByTimeTable(TimeTable timeTable);
}