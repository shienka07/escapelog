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

    private String nickname;

    private Long memberNo;

    private LocalDate regdate;


    public RecodeDto(String nickname, boolean secret, String title, String contents, LocalDate regdate, Boolean success, Integer rating) {
        this.nickname = nickname;
        this.secret = secret;
        this.title = title;
        this.contents = contents;
        this.regdate = regdate;
        this.success = success;
        this.rating = rating;
    }

    public static RecodeDto reviewForm(Recode recode){
        return new RecodeDto(recode.getMember().getNickname(),recode.getSecret(),recode.getTitle(), recode.getContents(),
                recode.getRegdate().toLocalDate(),recode.getSuccess(),recode.getRating());
    }


}
