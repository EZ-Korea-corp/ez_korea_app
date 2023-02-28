package com.ezkorea.hybrid_app.web.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class OpinetApiRestTemplate {

    @Value("${opinet.api.key}")
    private String REST_API_KEY;
    private final int GASOLINE_NUM = 1;
    private final int DIESEL_NUM = 2;

    @Scheduled(cron = "0 0 6 * * ?")
    public void saveTodayFuelCost() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String url = "http://www.opinet.co.kr/api/avgAllPrice.do?code=%s&out=json".formatted(REST_API_KEY);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JSONObject jsonObject = convertToJson(response.getBody());

        int gasolinePriceAvg = getFuelCost(jsonObject, GASOLINE_NUM);
        System.out.println(gasolinePriceAvg);
        int dieselPriceAvg = getFuelCost(jsonObject, DIESEL_NUM);
        System.out.println(dieselPriceAvg);

    }

    public static JSONObject convertToJson(String jsonString) {
        JSONObject jsonObject = null;
        try {
            // JSON 데이터를 파싱하여 JSONObject로 변환
            JSONParser jsonParser = new JSONParser();
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public int getFuelCost(JSONObject jsonObject, int sort) {
        JSONObject result = (JSONObject) jsonObject.get("RESULT");
        JSONObject gasoline = ((JSONObject) ((JSONArray) result.get("OIL")).get(sort));
        String price = gasoline.get("PRICE").toString().substring(0, 4);
        return Integer.parseInt(price);
    }

}
