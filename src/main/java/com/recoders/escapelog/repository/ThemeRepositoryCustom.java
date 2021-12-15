package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Theme;

import java.util.List;

public interface ThemeRepositoryCustom {
    List<Theme> searchTheme(String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
}
