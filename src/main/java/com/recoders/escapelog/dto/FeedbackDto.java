package com.recoders.escapelog.dto;
import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.FeedbackType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedbackDto {

    private long userNo;
    private FeedbackType feedbackType;
    private String contents;

    private AreaType areaType;
    private String newThemeName;
    private long themeNo;

}
