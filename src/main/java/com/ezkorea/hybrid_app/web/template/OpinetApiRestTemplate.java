package com.ezkorea.hybrid_app.web.template;

import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCost;
import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCostRepository;
import lombok.RequiredArgsConstructor;
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

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OpinetApiRestTemplate {

    @Value("${opinet.api.key}")
    private String REST_API_KEY;
    private final int GASOLINE_NUM = 1;
    private final int DIESEL_NUM = 2;
    private final FuelCostRepository fcRepository;

    /**
     * 매일 새벽 00시 00분에 실행되는 메소드
     * "0 0 0 * * ?":
     * "초 분 시 일 월 요일" 순서로 표현한 cron 표현식
     * - *: 해당 필드의 모든 값
     * - ? : 해당 필드를 사용하지 않음
     * */
    @Scheduled(cron = "0 9 2 * * ?")
    public void saveTodayFuelCost() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String url = "http://www.opinet.co.kr/api/avgAllPrice.do?code=%s&out=json".formatted(REST_API_KEY);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        JSONObject jsonObject = convertToJson(response.getBody());

        int gasolinePriceAvg = getFuelCost(jsonObject, GASOLINE_NUM);
        int dieselPriceAvg = getFuelCost(jsonObject, DIESEL_NUM);

        fcRepository.save(FuelCost.builder()
                .gasolinePrice(gasolinePriceAvg)
                .dieselPrice(dieselPriceAvg)
                .baseDate(LocalDate.now())
                .build());

    }

    /**
     * Json 형태의 문자열을 JSONObject로 변환하는 함수
     * @param jsonString REST API를 통해 받아온 유가 정보 문자열
     * @return String to JSONObject
     * */
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

    /**
     * JSON 객체에서 유가 정보를 받아오는 함수
     * @param jsonObject 유가 정보가 담긴 JSONObject
     * @param sort 휘발유와 경유를 나누기 위한 변수(1 : 휘발유, 2 : 경유)
     * @return 구분에 맞는 평균 리터 가격
     * */
    public int getFuelCost(JSONObject jsonObject, int sort) {
        JSONObject result = (JSONObject) jsonObject.get("RESULT");
        JSONObject gasoline = ((JSONObject) ((JSONArray) result.get("OIL")).get(sort));
        String price = gasoline.get("PRICE").toString().substring(0, 4);
        return Integer.parseInt(price);
    }

}
