package com.ezkorea.hybrid_app.domain.adjustment;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.web.dto.AdjustmentDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
/* 통계용 테이블 */
public class Adjustment extends BaseEntity {

    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL)
    @Setter
    private List<LowPerformer> lowPerformerList = new ArrayList<>();

    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL)
    @Setter
    private List<DayOffMember> dayOffMemberList = new ArrayList<>();

    // 팀 번호
    private long teamNo;

    // 지점 번호
    private long divisionNo;

    // 현금 매출
    private int cashAdj;

    // 카드 매출
    private int cardAdj;

    // 평균 매출
    private int teamAvg;

    // 전체 매출
    private int totalAdj;

    // 이체 매출
    private int accountAdj;

    // 저조자
    @Column(length = 1000)
    @Setter
    private String lowFormAdj;

    // 휴무자
    @Column(length = 1000)
    @Setter
    private String dayOffAdj;

    // 정산 내용
    @Column(length = 10000)
    private String contentAdj;

    // 정산 날짜
    private LocalDate adjDate;

    public AdjustmentDto of() {
        return AdjustmentDto.builder()
                .teamNo(teamNo)
                .divisionNo(divisionNo)
                .cashAdj(cashAdj)
                .cardAdj(cardAdj)
                .teamAvg(teamAvg)
                .totalAdj(totalAdj)
                .accountAdj(accountAdj)
                .contentAdj(contentAdj)
                .dayOffAdj(dayOffAdj)
                .lowFormAdj(lowFormAdj)
                .build();
    }

    public AdjustmentDto of2() {
        return AdjustmentDto.builder()
                .teamNo(0)
                .divisionNo(0)
                .cashAdj(0)
                .cardAdj(0)
                .teamAvg(0)
                .totalAdj(0)
                .accountAdj(0)
                .contentAdj("")
                .dayOffAdj("")
                .lowFormAdj("")
                .build();
    }

    public void updateAdj(AdjustmentDto dto) {
        this.cashAdj = dto.getCashAdj();
        this.cardAdj = dto.getCardAdj();
        this.teamAvg = dto.getTeamAvg();
        this.totalAdj = dto.getTotalAdj();
        this.accountAdj = dto.getAccountAdj();
        this.contentAdj = dto.getContentAdj();
        this.lowFormAdj = dto.getLowFormAdj();
        this.dayOffAdj = dto.getDayOffAdj();
    }
}
