package com.ezkorea.hybrid_app.domain.etc.msgToCeo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MsgToCeoRepository extends JpaRepository<MsgToCeo, Long> {
    Page<MsgToCeo> findAllByOrderByCreateDateDesc(Pageable pageable);
    void deleteById(Long id);

}
