package com.ezkorea.hybrid_app.web.controller.rest.manager;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.MemberStatus;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
import com.ezkorea.hybrid_app.web.dto.MemberUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
@Slf4j
public class ManagerDivisionRestController {

    private final TeamService tService;
    private final MemberService mService;
    private final DivisionService dService;
    private final SaleService saleService;
    private final ModelMapper mapper;

    @PostMapping("/division")
    public ResponseEntity<Object> createNewDivision(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");

        DivisionDto dto = dService.createDivisionDto(teamName, teamGm);
        if (dto == null) {
            return new ResponseEntity<>(Map.of("message", "무소속 지점은 1개만 만들 수 있습니다."), HttpStatus.BAD_REQUEST);
        }

        dService.saveNewDivision(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @PutMapping("/division/{id}")
    public ResponseEntity<Object> updateDivision(@RequestBody Map<String, Object> datum, @PathVariable Long id) {

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");
        dService.updateDivision(id, teamName, teamGm);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

}
