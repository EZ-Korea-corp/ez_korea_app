package com.ezkorea.hybrid_app.domain.note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatchNoteRepository extends JpaRepository<PatchNote, Long> {

    Page<PatchNote> findAllByOrderByCreateDateDesc(Pageable pageable);

}
