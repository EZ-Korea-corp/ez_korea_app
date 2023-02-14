package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionService {

    private final DivisionRepository divisionRepository;

    public Division saveNewDivision(DivisionDto dto) {
        return divisionRepository.save(Division.builder()
                .divisionName(dto.getTeamName())
                .leader(dto.getTeamGm())
                .build());
    }

}
