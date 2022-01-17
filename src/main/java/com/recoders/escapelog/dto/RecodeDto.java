package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.BreakTime;
import com.recoders.escapelog.domain.Recode;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


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

    private LocalDateTime regdate;

    private Long no;

    private String imagePath;

}
