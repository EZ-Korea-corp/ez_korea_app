package com.ezkorea.hybrid_app.domain.gas;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GasStationRepository extends JpaRepository<GasStation, Long> {
    Optional<GasStation> findByStationName(String name);
}
