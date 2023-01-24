package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class DailyTask extends BaseEntity {
    private LocalDate taskDate;
    private boolean isClose;

}
