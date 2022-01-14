package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Theme;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Setter
@Getter
public class ThemeBasicDto {

    @NotBlank private String themeName;
    @NotBlank private String shopName;
    private AreaType areaType;
    @NotBlank private String detailArea;
    private boolean openStatus;
    @NotNull @Positive private int playTime;
    private String shopUrl;
    private String story;
    private String filePath;
    @Min(0) @Max(10) private int level;

    public Theme toEntity(){

        Theme build = Theme.builder()
                .themeName(themeName)
                .shopName(shopName)
                .shopUrl(shopUrl == null ? "" : shopUrl)
                .areaType(areaType)
                .detailArea(detailArea)
                .openStatus(openStatus)
                .playTime(playTime)
                .level(level)
                .story(story == null ? "" : story.replaceAll("<br>",System.getProperty("line.separator")))
                .filePath(filePath == null ? "" : filePath)
                .build();
        return build;
    }
}
