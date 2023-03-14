package com.ezkorea.hybrid_app.web.controller.rest.manager;

import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.service.user.team.TeamService;
import com.ezkorea.hybrid_app.web.dto.TeamDto;
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
public class ManagerTeamRestController {

    private final TeamService tService;
    private final DivisionService dService;
    private final ModelMapper mapper;

    @PostMapping("/team")
    public ResponseEntity<Object> createNewTeam(@RequestBody Map<String, Object> datum) {
        log.info("datum={}", datum.toString());
        String teamName = (String) datum.get("teamName");
        String divisionName = (String) datum.get("teamGM");
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");

        Division currentDivision = dService.findDivisionByDivisionName(divisionName);
        TeamDto dto = tService.createTeamDto(currentDivision, teamName, teamLeader, teamEmployee);
        tService.saveNewTeam(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }



    @PutMapping("/team/{id}")
    public ResponseEntity<Object> modifyTeam(@RequestBody Map<String, Object> datum, @PathVariable Long id) {

        log.info("datum={}", datum.toString());
        String teamName = (String) datum.get("teamName");
        String divisionName = (String) datum.get("teamGM");
        Division currentDivision = dService.findDivisionByDivisionName(divisionName);
        String teamLeader = (String) datum.get("teamLeader");
        String teamEmployee = (String) datum.get("teamEmployee");

        tService.updateTeam(id, currentDivision, teamName, teamLeader, teamEmployee);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

    @DeleteMapping("/team/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable Long id) {

        tService.removeTeam(id);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

}
