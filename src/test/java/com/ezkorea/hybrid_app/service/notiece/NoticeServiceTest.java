package com.ezkorea.hybrid_app.service.notiece;

import com.ezkorea.hybrid_app.app.util.ResponseData;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.notice.NoticeRepository;
import com.ezkorea.hybrid_app.web.dto.NoticeDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class NoticeServiceTest {

    @Autowired
    private NoticeRepository repository;

    @Autowired
    private ModelMapper mapper;

    @BeforeEach
    public void testDataInit() {

        repository.save(Notice.builder()
                .title("title")
                .content("content")
                .build());

    }

    @Test
    @Order(1)
    @DisplayName("저장된 데이터 DTO 변환")
    public void findNoticeDataTest() {
        Notice findNotice = repository.findById(1L).get();
        NoticeDto dto = mapper.map(findNotice, NoticeDto.class);
        System.out.println("dto = " + dto);
        assertThat(dto.getId()).isEqualTo(1L);
    }

    @Test
    @Order(2)
    @DisplayName("DTO to ResponseData")
    public void mapNoticeToDtoTest() {
        NoticeDto dto = mapper.map(repository.findById(1L).get(), NoticeDto.class);
        ResponseData.ApiResult<NoticeDto> dtoApiResult = ResponseData.success(dto, "저장되었습니다.");
        assertThat(dtoApiResult.isSuccess()).isTrue();
        System.out.println(dtoApiResult);
    }

}