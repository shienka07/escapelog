package com.escapelog.escapelog.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue()
    private Long no;

    private String id;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    private String grade;

    private String email;

    private boolean emailVerified;

    private String emailCheckToken;

}
