package com.ezkorea.hybrid_app.domain.gas;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
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

    @OneToMany(mappedBy = "gasStation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<S3Image> imageList = new ArrayList<>();
}
