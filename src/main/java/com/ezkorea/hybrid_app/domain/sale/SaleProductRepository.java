package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.task.DailyTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleProductRepository extends JpaRepository<SaleProduct, Long> {

    List<SaleProduct> findAllByTaskAndStatus(DailyTask task, String status);

    void deleteByTaskAndStatus(DailyTask task, String status);
}