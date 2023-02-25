package com.ezkorea.hybrid_app.web.controller.rest;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ezkorea.hybrid_app.service.AWSService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AWSRestController {
    private final AWSService awsService;

    @GetMapping("/image/upload")
    public ResponseEntity<Object> upload(MultipartFile[] multipartFileList) {

        List<String> imagePathList = awsService.saveImage(multipartFileList);

        return new ResponseEntity<Object>(imagePathList, HttpStatus.OK);
    }

}
