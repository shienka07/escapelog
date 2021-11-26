package com.escapelog.escapelog.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Calendar {

    @Id
    @GeneratedValue
    private Long no;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String title;

    private String contents;

    private IconType iconType;
}
