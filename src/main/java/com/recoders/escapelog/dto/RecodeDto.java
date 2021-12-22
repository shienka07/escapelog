package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.BreakTime;
import com.recoders.escapelog.domain.Recode;
import com.recoders.escapelog.domain.Theme;
import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class RecodeDto {

    @NotNull
    private String title;

    private Long themeNo;

    private String contents;

    private Integer rating;

    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean secret;

    private String nickname;

    private Long memberNo;

    private LocalDate regdate;

    private Long no;

    public RecodeDto(Long no, String nickname, boolean secret, String title, String contents, LocalDate regdate, Boolean success, Integer rating) {
        this.no = no;
        this.nickname = nickname;
        this.secret = secret;
        this.title = title;
        this.contents = contents;
        this.regdate = regdate;
        this.success = success;
        this.rating = rating;
    }

    public static RecodeDto reviewForm(Recode recode){
        return new RecodeDto(recode.getNo(),recode.getMember().getNickname(),recode.getSecret(),recode.getTitle(), recode.getContents(),
                recode.getRegdate().toLocalDate(),recode.getSuccess(),recode.getRating());
    }


}
