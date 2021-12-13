package com.recoders.escapelog.domain;

import com.recoders.escapelog.domain.GradeType;
import com.recoders.escapelog.domain.MemberType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(unique = true)
    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @Enumerated(EnumType.STRING)
    private GradeType gradeType;

    private LocalDateTime joinedAt;

    private boolean emailVerified;

    private String emailCheckToken;

    private String authenticationCode;


}
