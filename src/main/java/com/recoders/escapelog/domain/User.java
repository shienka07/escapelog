package com.recoders.escapelog.domain;

import lombok.*;

import javax.persistence.*;

//@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String grade;

    private boolean emailVerified;

    private String emailCheckToken;

}
