package com.recoders.escapelog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String themeName;

    private String shopName;

    private String shopUrl;

    private Integer playTime;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    private String detailArea;

    private Boolean openStatus;

    private Integer level;

    @Column(columnDefinition = "TEXT")
    private String story;

}
