package com.ezkorea.hybrid_app.web.template;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.aws.S3ImageRepository;
import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.expenses.fuel.FuelCost;
import com.ezkorea.hybrid_app.service.aws.AWSService;
import com.ezkorea.hybrid_app.service.expenses.ExpensesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ImageRestTemplate {

    private final ExpensesService expensesService;
    private final AWSService awsService;
    private final S3ImageRepository s3Repository;
    /**
     * 2주 간격 월요일마다 진행되는 함수
     */
    @Scheduled(cron = "0 0 0 ? * MON#2")
    @Transactional
    public void removeExpensesS3Image() {
        List<Expenses> hasImageExpensesList = expensesService.findHasImageExpensesList();
        for (Expenses expenses : hasImageExpensesList) {
            S3Image s3Image = expenses.getS3Image();
            awsService.deleteS3Image(s3Image, false);
            s3Image.setFilePath("");
            s3Image.setFileName("");
            s3Image.setFileRepo("");
            s3Repository.save(s3Image);
        }
    }
}
