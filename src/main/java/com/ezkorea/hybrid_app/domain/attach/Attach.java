package com.ezkorea.hybrid_app.domain.attach;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class Attach extends BaseEntity {

    private String fileId;

    private String fileName;

    private String path;

}
