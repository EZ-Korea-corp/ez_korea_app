package com.ezkorea.hybrid_app.domain.aws;

import org.springframework.data.jpa.repository.JpaRepository;

public interface S3ImageRepository extends JpaRepository<S3Image, Long> {

}
