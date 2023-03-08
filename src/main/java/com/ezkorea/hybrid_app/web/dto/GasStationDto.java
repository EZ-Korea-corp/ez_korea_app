package com.ezkorea.hybrid_app.web.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class GasStationDto {
    private Long id;
    private String stationName;
    private String stationLocation;
    private String memo;
    private int distance ;
    private List<MultipartFile> files;
    private List<Long> delFiles;

}
