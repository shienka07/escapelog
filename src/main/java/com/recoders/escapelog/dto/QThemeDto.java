package com.recoders.escapelog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class QThemeDto {

    private long no;
    private String themeName;
    private String shopName;
    private String imagePath;
    private Boolean openStatus;
    private Boolean success;

}
