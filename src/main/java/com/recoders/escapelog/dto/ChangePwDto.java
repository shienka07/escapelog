package com.recoders.escapelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwDto {

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z\\d~!@#$%^&*()+|=].{7,29}$",
            message="비밀번호를 정확하게 입력해주세요."
    )
    private String password;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])[A-Za-z\\d~!@#$%^&*()+|=].{7,29}$",
            message="비밀번호는 영어와 숫자 조합으로 이루어져야 합니다.(8-30자)"
    )
    private String newPassword;

    private String rePassword;
}
