package com.ezkorea.hybrid_app.web.controller.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoMapsApiRestTemplateTest {

    @Autowired
    private KakaoMapsApiRestTemplate template;

    @Autowired
    private OpinetApiRestTemplate opinetApiRestTemplate;

    private final String lat = "37.2976181";
    private final String lng = "126.9714921";

    void printPrettyJson(Map<String, Object> json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            System.out.println(prettyJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("현재 위치 기준 모든 정보를 확인하는 테스트")
    void getAllInfoJson() {
        Map<String, Object> objectMap = template.getCurrentPosInfo(lat, lng);
        printPrettyJson(objectMap);
    }

    @Test
    @DisplayName("현재 위치의 도로명을 확인하는 테스트")
    void getAddressName() {
        Map<String, Object> objectMap = template.getCurrentPosInfo(lat, lng);
        printPrettyJson(objectMap);

        org.json.simple.JSONObject jsonObject = template.getJsonObject(objectMap);
        String addressName = template.getAddressName(jsonObject);
        System.out.println("addressName = " + addressName);

        assertThat(addressName).isEqualTo("경기 수원시 장안구 율전동 433-2");
    }

    @Test
    @DisplayName("현재 위치에서 회사까지 거리를 확인하는 테스트")
    void getDistance() {
        Map<String, Object> objectMap = template.getDistanceFromCompany(lat, lng);
        printPrettyJson(objectMap);

        org.json.simple.JSONObject jsonObject = template.getJsonObject(objectMap);
        String distance = template.getDistance(jsonObject);
        System.out.println("distance = " + distance);

        assertThat(distance).isEqualTo("3734");
    }

    @Test
    @DisplayName("유류 정보를 확인하는 테스트")
    void getFuel() {
        opinetApiRestTemplate.saveTodayFuelCost();
    }

}