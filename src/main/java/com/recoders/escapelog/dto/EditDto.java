package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.BreakTime;
import com.recoders.escapelog.domain.Theme;
import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class EditDto {

    @NotNull
    private String title;

    private Theme theme;

    private Long themeNo;

    private String contents;

    private Integer rating;

    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean secret;

    private String imagePath;

    private Boolean imageChanges;
}
