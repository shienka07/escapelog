package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.Theme;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Getter
public class ThemeDto {

    private long themeNo;
    private String themeName;
    private String shopName;
    private String imageUrl;

    private int playTime;
    private int level;
    private boolean openStatus;
    private String shopUrl;
    private String story;
    private Map<Integer,Integer> ratingMap;

    public ThemeDto(Long no, String themeName, String shopName, String imageUrl, Boolean openStatus) {
        this.themeNo = no;
        this.themeName = themeName;
        this.shopName = shopName;
        this.imageUrl = imageUrl;
        this.openStatus = openStatus;
    }

    public static ThemeDto simpleForm(Theme theme){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl(),theme.getOpenStatus());
    }


    public static ThemeDto detailForm(Theme theme,Map<Integer, Integer> ratingMap){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl()
                    ,theme.getPlayTime(),theme.getLevel(), theme.getOpenStatus(), theme.getShopUrl(), theme.getStory(), ratingMap);
    }

}
