package com.recoders.escapelog.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Enumerated(EnumType.STRING)
    private AreaType areaType;

    @Enumerated(EnumType.STRING)
    private FeedbackType feedbackType;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(targetEntity = Theme.class, fetch = FetchType.LAZY)
    private Theme theme;

    private String newThemeName;

    private String contents;

    private LocalDate regdate;
}
