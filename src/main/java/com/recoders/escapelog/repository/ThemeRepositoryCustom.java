package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.ThemeDto;

import java.util.List;

public interface ThemeRepositoryCustom {
    List<Theme> searchTheme(String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<Theme> searchThemeKeyword(String keyword);
    List<ThemeDto> findAllTheme(Member member);
    List<ThemeDto> searchTheme(Member member,String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<ThemeDto> searchThemeStampExclude(Member member,String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
}
