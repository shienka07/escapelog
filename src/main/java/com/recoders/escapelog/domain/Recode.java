package com.recoders.escapelog.domain;

import com.recoders.escapelog.dto.EditDto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Recode {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(targetEntity = Theme.class, fetch = FetchType.LAZY)
    private Theme theme;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String contents;

    @Transient
    private String nlString;

    @NonNull
    private LocalDateTime regdate;

    private Integer rating;

    @Embedded
    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean playerCheck;

    private Long viewCount;

    private Boolean secret;

    private String imagePath;


    public void update(EditDto editDto) {
        if(editDto.getTheme().getNo() != null) {
            this.theme = editDto.getTheme();
        }else{
            this.theme = null;
        }
        this.title = editDto.getTitle();
        this.contents = editDto.getContents();;
        this.rating = editDto.getRating();
        this.breakTime = editDto.getBreakTime();
        this.hint = editDto.getHint();
        this.success = editDto.getSuccess();
        this.playerNum = editDto.getPlayerNum();
        this.secret = editDto.getSecret();
        this.imagePath = editDto.getImagePath();

    }

}
