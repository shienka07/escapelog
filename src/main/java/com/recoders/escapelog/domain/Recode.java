package com.recoders.escapelog.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Data
public class Recode {

    @Id @GeneratedValue
    private Long no;

    //    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(targetEntity = Theme.class, fetch = FetchType.LAZY)
    private Theme theme;

    private String title;

    private String contents;

    @Transient
    private String nlString;

    @NotNull
    private LocalDateTime regdate;

    private Integer rating;

    @Embedded
    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean playerCheck;

    private Long viewCount;


    @Builder
    public Recode() {
        regdate = LocalDateTime.now();
    }
}
