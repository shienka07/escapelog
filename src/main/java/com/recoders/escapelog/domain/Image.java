package com.recoders.escapelog.domain;

import javax.persistence.*;

//@Entity
public class Image {
    @Id
    @GeneratedValue
    private Long no;

    @ManyToOne(targetEntity = Recode.class, fetch = FetchType.LAZY)
    private Recode recode;

    private String imageUrls;
}
