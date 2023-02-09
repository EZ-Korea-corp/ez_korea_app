package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.service.etc.AttachService;
import com.ezkorea.hybrid_app.service.sales.GasStationService;
import com.ezkorea.hybrid_app.service.sales.SaleService;
import com.ezkorea.hybrid_app.web.dto.GasStationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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

        // 파일저장
        if(dto.getFiles() != null && dto.getFiles().size() > 0) {
            String path = request.getSession().getServletContext().getRealPath("resources")
                        + "\\uploadFiles" + "\\station" + "\\" + stationId;
            log.info("==============upload path=======---========");
            log.info(path);
            log.info("===========================================");

            attachService.saveAttach(path, dto.getFiles(), "station" + stationId);
        }

        returnMap.put("stationId", stationId);

        return returnMap;
    }
}
