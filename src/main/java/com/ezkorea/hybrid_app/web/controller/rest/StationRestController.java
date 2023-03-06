package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.GasStationDto;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StationRestController {
    private final GasStationService gasStationService;

    @PostMapping("/station/saveStation")
    public Map<String, Object> saveStation(GasStationDto dto) {
        Map<String, Object> returnMap = new HashMap<>();

        long stationId = gasStationService.saveGasStation(dto);
        returnMap.put("stationId", stationId);

        return returnMap;
    }

    @PostMapping("/station")
    public ResponseEntity<Object> saveNewStation(@RequestBody GasStationDto dto) {

        long stationId = gasStationService.saveGasStation(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", stationId), HttpStatus.OK);
    }

    @PutMapping("/station/{id}")
    public ResponseEntity<Object> updateStation(@PathVariable Long id, @RequestBody GasStationDto dto) {
        GasStation updateStation = gasStationService.updateGasStation(id, dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", updateStation.getId()), HttpStatus.OK);
    }

    @PostMapping("/station/restDetail")
    public Map<String, Object> showGasStationDetailPage(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();

        GasStation station = gasStationService.findStationById(Long.valueOf((String) data.get("id")));
        List<S3Image> s3ImageList = station.getImageList();
        station.setImageList(null);

        returnMap.put("station", station);
        returnMap.put("s3ImageList", s3ImageList);
        return returnMap;
    }

    @PutMapping("/station/isWork")
    public ResponseEntity<Object> updateStationIsWork(@RequestBody Map<String, Object> paramMap) {
        GasStation updateStation = gasStationService.updateGasStationIsWork(paramMap);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", updateStation.getId()), HttpStatus.OK);
    }

}
