package com.ezkorea.hybrid_app.domain.sale;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Wiper wiper;

    @ManyToOne
    @Setter
    private GasStation gasStation;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @Setter
    private Member member;

    @CreatedDate
    private LocalDate date;

    private int count;
}
