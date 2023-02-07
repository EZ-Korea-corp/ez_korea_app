package com.ezkorea.hybrid_app.web.controller.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class KakaoMapsApiTestTemplateTest {

    @Autowired
    private KakaoMapsApiTestTemplate template;

    private final String lat = "37.2976181";
    private final String lng = "126.9714921";

    @Test
    void getLocationData() throws JsonProcessingException, JSONException {
        Map<String, Object> objectMap = template.getDistanceFromCompany(lat, lng);
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writeValueAsString(objectMap);
        JSONObject json = new JSONObject(jsonStr);
        JSONArray documents = json.getJSONArray("documents");
        JSONObject object = documents.getJSONObject(0);
        String distance = object.getString("distance");
        System.out.println("json object = " + object);
        System.out.println("distance = " + distance);
    }

    @Test
    void getAddressName() {
        Map<String, Object> objectMap = template.getCurrentPosInfo(lat, lng);
        org.json.simple.JSONObject jsonObject = template.getJsonObject(objectMap);
        String addressName = template.getAddressName(jsonObject);
        System.out.println("addressName = " + addressName);

        assertThat(addressName).isEqualTo("경기 수원시 장안구 율전동 433-2");
    }

    @Test
    void getDistance() {
        Map<String, Object> objectMap = template.getDistanceFromCompany(lat, lng);
        org.json.simple.JSONObject jsonObject = template.getJsonObject(objectMap);
        System.out.println("jsonObject = " + jsonObject);
        String distance = template.getDistance(jsonObject);
        System.out.println("distance = " + distance);

        assertThat(distance).isEqualTo("3734");
    }

}