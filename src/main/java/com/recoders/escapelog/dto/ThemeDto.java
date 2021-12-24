package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.Theme;
import lombok.*;

import java.util.Map;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDto {

    @NonNull private long no;
    @NonNull private String themeName;
    @NonNull private String shopName;
    @NonNull private String imageUrl;
    @NonNull private boolean openStatus;
    private Boolean success;
    private int playTime;
    private int level;
    private String shopUrl;
    private String story;
    private int totalRatingNum;
    private Map<Integer,Integer> ratingMap;

    public ThemeDto(Long no, String themeName, String shopName, String imageUrl, Boolean openStatus, Integer playTime, Integer level, String shopUrl, String story, int totalRatingNum, Map<Integer, Integer> ratingMap) {
        this.no = no;
        this.themeName = themeName;
        this.shopName = shopName;
        this.imageUrl = imageUrl;
        this.openStatus = openStatus;
        this.playTime = playTime;
        this.level = level;
        this.shopUrl = shopUrl;
        this.story = story;
        this.totalRatingNum = totalRatingNum;
        this.ratingMap = ratingMap;
    }

    public static ThemeDto simpleForm(Theme theme){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl(),theme.getOpenStatus());

    }

    public static ThemeDto detailForm(Theme theme,int totalRatingNum, Map<Integer, Integer> ratingMap){
        return new ThemeDto(theme.getNo(), theme.getThemeName(), theme.getShopName(), theme.getImageUrl(),theme.getOpenStatus()
                    ,theme.getPlayTime(),theme.getLevel(),theme.getShopUrl(), theme.getStory(),
                    totalRatingNum, ratingMap);
    }

}
