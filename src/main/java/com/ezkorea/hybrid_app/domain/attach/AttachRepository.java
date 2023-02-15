package com.ezkorea.hybrid_app.domain.attach;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    List<Attach> findAllByFileId(String id);
}
