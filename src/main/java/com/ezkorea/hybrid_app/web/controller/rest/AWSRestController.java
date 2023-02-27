package com.ezkorea.hybrid_app.web.controller.rest;

import com.ezkorea.hybrid_app.service.aws.AWSService;
import com.ezkorea.hybrid_app.web.dto.S3ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AWSRestController {
    private final AWSService awsService;

    private final ModelMapper modelMapper;

    @PutMapping("/image/upload")
    public ResponseEntity<Object> upload(@RequestParam(value = "files", required = false) List<MultipartFile> multipartFile,
                                         @RequestParam Map<String, Object> params) {

        S3ImageDto dto = modelMapper.map(params, S3ImageDto.class);

        awsService.saveImageInCurrentEntity(dto, multipartFile);

        return new ResponseEntity<>(Map.of("message", "반영되었습니다"), HttpStatus.OK);
    }

}
