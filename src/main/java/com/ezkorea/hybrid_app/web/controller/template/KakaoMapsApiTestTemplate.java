package com.ezkorea.hybrid_app.web.controller.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class KakaoMapsApiTestTemplate {

    @Value("${kakao.api.key}")
    private String REST_API_KEY;
    private final String COMPANY_LAT = "37.31095081549452";
    private final String COMPANY_LNG = "127.01021456882813";

    public String getCurrentLocation(String location) {
        String currentLat = location.split(",")[0];
        String currentLng = location.split(",")[1];
        return getAddressName(getJsonObject(getCurrentPosInfo(currentLat, currentLng)));
    }

    public Map<String, Object> getDistanceFromCompany(String currentLat, String currentLng) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?x={currentLng}&y={currentLat}&radius=20000&query=이제트코리아";
        return getRestTemplateResult(url, currentLat, currentLng);
    }

    public Map<String, Object> getCurrentPosInfo(String currentLat, String currentLng) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%s&y=%s".formatted(currentLng, currentLat);
        return getRestTemplateResult(url, currentLat, currentLng);
    }

    public Map<String, Object> getRestTemplateResult(String url, String currentLat, String currentLng) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + REST_API_KEY);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class, currentLng, currentLat);
        return response.getBody();
    }

    public JSONObject getJsonObject(Map<String, Object> resultData) {
        Map<String, Object> objectMap = resultData;
        ObjectMapper mapper = new ObjectMapper();
        JSONObject datum;
        try {
            String jsonStr = mapper.writeValueAsString(objectMap);
            JSONObject json = (JSONObject) JSONValue.parse(jsonStr);
            JSONArray documents = (JSONArray) json.get("documents");
            datum = (JSONObject) documents.get(0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return datum;
    }

    public String getAddressName(JSONObject json) {
        JSONObject address = (JSONObject) json.get("address");
        return (String) address.get("address_name");
    }

    public String getDistance(JSONObject json) {
        return (String) json.get("distance");
    }

}
