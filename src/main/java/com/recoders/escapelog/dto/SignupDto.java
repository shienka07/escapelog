package com.recoders.escapelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {

    @Email(message = "이메일 형식이 아닙니다.")
    @NotBlank
    private String email;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z가-힣])[a-zA-Z가-힣\\d\\s]{2,12}$",
            message="닉네임은 영어 또는 한글이 1자 이상 포함되어야 합니다.(2-12자)"
    )
    private String nickname;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z\\d~!@#$%^&*()+|=].{7,29}$",
            message="비밀번호는 영어와 숫자 조합으로 이루어져야 합니다.(8-30자)"
    )
    private String password;


}
