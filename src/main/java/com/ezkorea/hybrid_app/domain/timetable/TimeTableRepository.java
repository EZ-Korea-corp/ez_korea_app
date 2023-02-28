package com.ezkorea.hybrid_app.domain.timetable;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long> {

    TimeTable findByTaskDateAndPartAndGasStationAndMember(LocalDate date, String part, GasStation station, Member member);

    List<TimeTable> findAllByTaskDateAndMemberAndPartNot(LocalDate date, Member member, String part);

    List<TimeTable> findAllByTaskDateAndGasStationAndPart(LocalDate date, GasStation station, String part);
}