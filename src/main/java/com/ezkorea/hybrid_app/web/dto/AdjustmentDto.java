package com.ezkorea.hybrid_app.web.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class AdjustmentDto {

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
    // 정산 내용
    private String contentAdj;
    // 휴무자
    private String dayOffAdj;
    // 저조자
    private String lowFormAdj;

    // 정산 날짜
    private LocalDate adjDate;

}
