package com.ezkorea.hybrid_app.domain.wiper;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Wiper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wiperSort;
    private String wiperSize;
    private int wiperPrice;
    private String wiperViewName;

}
