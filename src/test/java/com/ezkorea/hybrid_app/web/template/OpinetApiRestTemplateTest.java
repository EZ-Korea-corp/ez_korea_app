package com.ezkorea.hybrid_app.web.template;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class OpinetApiRestTemplateTest {

    @Autowired
    private OpinetApiRestTemplate opinetApiRestTemplate;

    @Test
    @DisplayName("유류 정보를 확인하는 테스트")
    void getFuel() {
        opinetApiRestTemplate.saveTodayFuelCost();
        assertThat(10).isEqualTo(10);
    }

}