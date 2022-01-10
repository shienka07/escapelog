package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Theme;
import lombok.Getter;
import lombok.Setter;

@Setter
public class ThemeBasicDto {

    private String themeName;
    private String shopName;
    private String shopUrl;
    private AreaType areaType;
    private String detailArea;

    private boolean openStatus;
    private int playTime;
    private int level;
    private String story;
    private String imageUrl;

    public Theme toEntity(){
        Theme build = Theme.builder()
                .themeName(themeName)
                .shopName(shopName)
                .shopUrl(shopUrl)
                .areaType(areaType)
                .detailArea(detailArea)
                .openStatus(openStatus)
                .playTime(playTime)
                .level(level)
                .story(story)
                .imageUrl(imageUrl)
                .build();
        return build;
    }
}
