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
public class KakaoMapsApiRestTemplate {

    @Value("${kakao.api.key}")
    private String REST_API_KEY;
    private final String COMPANY_LAT = "37.31095081549452";
    private final String COMPANY_LNG = "127.01021456882813";

    public String getCurrentLocation(String location) {
        String currentLat = location.split(",")[0];
        String currentLng = location.split(",")[1];
        return getAddressName(getJsonObject(getCurrentPosInfo(currentLat, currentLng)));
    }

    /**
     * 현재 위치에서 회사까지의 정보를 받아오는 메소드
     * @param currentLat JS를 통해 받아온 위도
     * @param currentLng JS를 통해 받아온 경도
     * @return RestTemplate을 통해 받아온 JSON(Map) 결과 반환
     * */
    public Map<String, Object> getDistanceFromCompany(String currentLat, String currentLng) {
        String url = "https://dapi.kakao.com/v2/local/search/keyword.json?x={currentLng}&y={currentLat}&radius=20000&query=이제트코리아";
        return getRestTemplateResult(url, currentLat, currentLng);
    }

    /**
     * 현재 위치에 대한 주소 정보를 받아오기 위한 메소드
     * @param currentLat JS를 통해 받아온 위도
     * @param currentLng JS를 통해 받아온 경도
     * @return RestTemplate을 통해 받아온 JSON(Map) 결과 반환
     * */
    public Map<String, Object> getCurrentPosInfo(String currentLat, String currentLng) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json?x={currentLng}&y={currentLat}";
        return getRestTemplateResult(url, currentLat, currentLng);
    }

    /**
     * Kakao Maps Api에 요청을 보내기 위한 함수
     * @param currentLat 사용자의 위도
     * @param currentLng 사용자의 경도
     * @return 조회를 통해 받아온 JSON(Map) 결과 반환
     * */
    public Map<String, Object> getRestTemplateResult(String url, String currentLat, String currentLng) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + REST_API_KEY);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class, currentLng, currentLat);
        return response.getBody();
    }

    /**
     * Kakao Maps Api 기준으로 받아온 Body를 JSONObject로 변환해주는 메소드
     * @param resultData RestTemplate을 통해 받아온 JSON(Map)
     * @return 받아온 데이터를 JSONObject 형태로 반환
     * */
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

    /**
     * JSONObject 형식으로 반환된 데이터 중에서 address_name만 받아오는 함수
     * @param json RestTemplate 요청을 통해 받아와 변환한 데이터
     * @return 사용자의 address_name을 반환
     * */
    public String getAddressName(JSONObject json) {
        JSONObject address = (JSONObject) json.get("address");
        return (String) address.get("address_name");
    }

    /**
     * JSONObject 형식으로 반환된 데이터 중에서 distance만 받아오는 함수
     * @param json RestTemplate 요청을 통해 받아와 변환한 데이터
     * @return 사용자의 위치에서 회사까지 distance을 반환
     * */
    public String getDistance(JSONObject json) {
        return (String) json.get("distance");
    }

}
