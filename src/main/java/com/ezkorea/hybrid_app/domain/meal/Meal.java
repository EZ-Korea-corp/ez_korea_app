package com.ezkorea.hybrid_app.domain.meal;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.web.dto.MealDto;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, exclude = "member")
public class Meal extends BaseEntity {

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @CreatedDate
    private LocalDate checkDate;

    public MealDto of() {
        return MealDto.builder()
                .name(member.getName())
                .phone(member.getPhone())
                .checkTime(getCreateDate())
                .build();
    }

}
