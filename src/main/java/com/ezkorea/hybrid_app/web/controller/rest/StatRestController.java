package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.service.stat.StatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/stat")
@RequiredArgsConstructor
@Slf4j
public class StatRestController {
    private final StatService statService;

    @PostMapping("/graph")
    public Map<String, Object> findStatList(@RequestBody Map<String, String> data) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();

        returnMap.put("result", statService.findStatList(data));
        return returnMap;
    }

}
