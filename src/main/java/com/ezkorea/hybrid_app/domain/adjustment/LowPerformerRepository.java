package com.ezkorea.hybrid_app.domain.adjustment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface LowPerformerRepository extends JpaRepository<LowPerformer, Long> {

}
