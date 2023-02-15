package com.ezkorea.hybrid_app.domain.wiper;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WiperRepository extends JpaRepository<Wiper, Long> {
    Wiper findByWiperSizeAndWiperSort(String size, String sort);

    List<Wiper> findAllByOrderByWiperSortAscWiperSizeAsc();
}
