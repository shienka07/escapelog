package com.recoders.escapelog.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

//@Entity
public class Recode {

    @Id @GeneratedValue
    private Long no;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(targetEntity = Theme.class, fetch = FetchType.LAZY)
    private Theme theme;

    private String title;

    private String contents;

    private LocalDateTime regdate;

    private Integer rating;

    @Embedded
    private BreakTime breakTime;

    private Integer hint;

    private Boolean success;

    private Integer playerNum;

    private Boolean playerCheck;

    private Long viewCount;

}
