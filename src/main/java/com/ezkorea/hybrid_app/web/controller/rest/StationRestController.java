package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.domain.gas.GasStation;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.user.member.SecurityUser;
import com.ezkorea.hybrid_app.service.etc.AttachService;
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
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StationRestController {
    private final SaleService saleService;
    private final GasStationService gasStationService;
    private final AttachService attachService;

    @PostMapping("/station/saveStation")
    public Map<String, Object> findStockList(GasStationDto dto, HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();

        long stationId = gasStationService.saveGasStation(dto);
        String path = request.getSession().getServletContext().getRealPath("resources");

        // 파일삭제
        attachService.deleteAttach(dto.getDelFiles(), path);

        // 파일저장
        if(dto.getFiles() != null && dto.getFiles().size() > 0) {
            path += "\\uploadFiles" + "\\station" + "\\" + stationId;
            log.debug("==================upload path==================");
            log.debug(path);

            attachService.saveAttach(path, dto.getFiles(), "station" + stationId);
        }

        returnMap.put("stationId", stationId);

        return returnMap;
    }

    @PostMapping("/station")
    public ResponseEntity<Object> saveNewStation(@RequestBody GasStationDto dto) {

        long stationId = gasStationService.saveGasStation(dto);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다", "id", stationId), HttpStatus.OK);
    }

    @PostMapping("/station/restDetail")
    public Map<String, Object> showGasStationDetailPage(@RequestBody Map<String, Object> data) {
        Map<String, Object> returnMap = new HashMap<>();

        GasStation station = gasStationService.findStationById(Long.valueOf((String)data.get("id")));

        returnMap.put("station", station);
        returnMap.put("attachList", attachService.findAttachList("station" + station.getId()));
        return returnMap;
    }

}
