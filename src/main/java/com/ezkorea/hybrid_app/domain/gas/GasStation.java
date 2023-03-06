package com.ezkorea.hybrid_app.domain.gas;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.web.dto.GasStationDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = "imageList")
@Builder
public class GasStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String stationName;
    private String stationLocation;
    private String memo;
    private int distance;
    private boolean isWork;

    public void setBasicInfo(GasStationDto dto) {
        this.stationName = dto.getStationName();
        this.stationLocation = dto.getStationLocation();
        this.memo = dto.getMemo();
        this.imageList.clear();
    }

    @OneToMany(mappedBy = "gasStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Image> imageList = new ArrayList<>();
}
