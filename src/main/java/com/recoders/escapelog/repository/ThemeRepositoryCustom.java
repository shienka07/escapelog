package com.recoders.escapelog.repository;

import com.querydsl.core.Tuple;
import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Member;
import com.recoders.escapelog.domain.Theme;
import com.recoders.escapelog.dto.ThemeRecodeDto;

import java.util.List;
import java.util.Map;

public interface ThemeRepositoryCustom {
    List<Theme> searchTheme(String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<Theme> searchThemeKeyword(String keyword);
    List<ThemeRecodeDto> findAllTheme(Member member);
    List<ThemeRecodeDto> searchTheme(Member member,String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
    List<ThemeRecodeDto> searchThemeStampExclude(Member member,String keyword, AreaType areaType, String detailArea, Boolean closeExclude);
}
