package com.escapelog.escapelog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Theme {

    @Id
    @GeneratedValue
    private Long no;

    private String themeName;

    private String shopName;

    private String shopUrl;

    private Integer playTime;

    private String imageUrl;

    private AreaType areaType;

    private String detailArea;

    private Boolean openStatus;

    private Integer level;

    private String story;
}
