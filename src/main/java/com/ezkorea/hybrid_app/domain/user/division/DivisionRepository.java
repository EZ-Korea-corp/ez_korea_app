package com.ezkorea.hybrid_app.domain.user.division;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    Division findByDivisionName(String divisionName);

}
