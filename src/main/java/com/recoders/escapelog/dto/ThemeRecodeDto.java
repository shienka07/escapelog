package com.recoders.escapelog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThemeRecodeDto {
    private Long themeNo;
    private String themeName;
    private String shopName;
    private String imageUrl;
    private boolean openStatus;
    private Boolean success;
}
