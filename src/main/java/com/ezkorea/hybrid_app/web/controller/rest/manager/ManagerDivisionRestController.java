package com.ezkorea.hybrid_app.web.controller.rest.manager;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.member.MemberService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.DivisionDto;
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

    private final DivisionService dService;
    private final SaleService saleService;
    private final ModelMapper mapper;

    @PostMapping("/division")
    public ResponseData.ApiResult<?> createNewDivision(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");
        String mainDivision = (String) datum.get("mainDivision");

        DivisionDto dto = dService.createDivisionDto(teamName, teamGm, mainDivision);

        dService.saveNewDivision(dto);

        return ResponseData.success(null, "반영되었습니다.");
    }

    @PutMapping("/division/{id}")
    public ResponseData.ApiResult<?> updateDivision(@RequestBody Map<String, Object> datum, @PathVariable Long id) {

        String teamName = (String) datum.get("teamName");
        String teamGm = (String) datum.get("teamGM");
        String mainDivision = (String) datum.get("mainDivision");
        dService.updateDivision(id, teamName, teamGm, mainDivision);

        return ResponseData.success(null, "반영되었습니다.");
    }

    @DeleteMapping("/division/{id}")
    public ResponseData.ApiResult<?> deleteTeam(@PathVariable Long id) {

        dService.removeDivision(id);

        return ResponseData.success(null, "반영되었습니다.");
    }

}
