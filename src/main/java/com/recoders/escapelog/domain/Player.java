package com.escapelog.escapelog.domain;

import javax.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long no;

    @ManyToOne(targetEntity = Recode.class, fetch = FetchType.LAZY)
    private Recode recode;

    private String playName;

    private String contents;
}
