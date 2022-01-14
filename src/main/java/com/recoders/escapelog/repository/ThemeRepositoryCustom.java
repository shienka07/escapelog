package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.*;
import com.recoders.escapelog.dto.QThemeDto;

import java.util.List;

public interface ThemeRepositoryCustom {
    List<Theme> searchTheme(String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<Theme> searchThemeKeyword(String keyword);
    List<QThemeDto> findAllTheme(Member member);
    List<QThemeDto> searchTheme(Member member, String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<QThemeDto> searchThemeStampExclude(Member member, String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
}
