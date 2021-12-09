package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ThemeDto {

    private long no;
    private String themeName;
    private String shopName;
    private String imageUrl;

    private int playTime;
    private int level;
    private boolean openStatus;
    private String shopUrl;
    private String story;

    public ThemeDto(Long no, String themeName, String shopName, String imageUrl, Boolean openStatus) {
        this.no = no;
        this.themeName = themeName;
        this.shopName = shopName;
        this.imageUrl = imageUrl;
        this.openStatus = openStatus;
    }

    public static ThemeDto simpleForm(Theme theme){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl(),theme.getOpenStatus());
    }

    public static ThemeDto detailForm(Theme theme){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl()
                    ,theme.getPlayTime(),theme.getLevel(), theme.getOpenStatus(), theme.getShopUrl(), theme.getStory());
    }

}
