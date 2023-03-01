package com.ezkorea.hybrid_app.service.user.division;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.exception.DivisionNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DivisionService {

    private final DivisionRepository divisionRepository;

    @Transactional
    public Division saveNewDivision(DivisionDto dto) {
        if (divisionRepository.existsByLeader(dto.getTeamGm())) {
            return divisionRepository.findByLeader(dto.getTeamGm());
        }
        Division savedDivision = divisionRepository.save(Division.builder()
                .divisionName(dto.getTeamName())
                .leader(dto.getTeamGm())
                .build());
        dto.getTeamGm().setDivision(savedDivision);
        return savedDivision;
    }

    public Division findDivisionByDivisionName(String divisionName) {
        return divisionRepository.findByDivisionName(divisionName);
    }

    public List<Division> findAllDivision() {
        return divisionRepository.findAll();
    }

    public Division findDivisionById(Long id) {
        return divisionRepository.findById(id)
                .orElseThrow( () -> new DivisionNotFoundException("해당 지점을 찾을 수 없습니다."));
    }
}
