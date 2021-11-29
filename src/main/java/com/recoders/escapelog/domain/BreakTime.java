package com.recoders.escapelog.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BreakTime {

    private Integer min;

    private Integer sec;
}
