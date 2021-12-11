package com.recoders.escapelog.domain;

import javax.persistence.*;

@Embeddable
public class Question {
    @Id
    @GeneratedValue
    private Long no;

    private AreaType areaType;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(targetEntity = Theme.class, fetch = FetchType.LAZY)
    private Theme theme;

    private String newThemeName;

    private String contents;

}
