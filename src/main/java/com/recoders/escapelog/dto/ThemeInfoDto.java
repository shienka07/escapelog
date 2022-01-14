package com.recoders.escapelog.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
public class ThemeInfoDto {

    private long no;
    private String themeName;
    private String shopName;
    private String imageUrl;
    private boolean openStatus;

    private int playTime;
    private int level;
    private int totalRatingNum;
    private Map<Integer,Integer> ratingMap;
    private String shopUrl;
    private String story;

    private Boolean success;

}
