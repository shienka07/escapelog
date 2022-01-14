package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.BreakTime;
import com.recoders.escapelog.domain.Recode;
import lombok.*;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class RecodeDto {

    @NonNull
    private String title;

    private Long themeNo;

    private String contents;

    private Integer rating;

    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean secret;

    private Long memberNo;

    private LocalDate regdate;

    private Long no;

    private String imagePath;

}
