package com.recoders.escapelog.repository;

import com.recoders.escapelog.domain.AreaType;
import com.recoders.escapelog.domain.Theme;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.util.List;

import static com.recoders.escapelog.domain.QTheme.theme;

@RequiredArgsConstructor
public class ThemeRepositoryImpl implements ThemeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Theme> searchThemeKeyword(String keyword){
        String searchKeyword = "%"+keyword+"%";
        return queryFactory
                .selectFrom(theme)
                .where(theme.themeName.like(searchKeyword)
                        .or(theme.shopName.like(searchKeyword)))
                .fetch();
    }

    @Override
    public List<Theme> searchTheme(String keyword,AreaType areaType, String detailArea, Boolean closeExclude){
        String searchKeyword = "%"+keyword+"%";
        return queryFactory
                .selectFrom(theme)
                .where(theme.themeName.like(searchKeyword)
                        .or(theme.shopName.like(searchKeyword))
                        .and(filterAreaType(areaType))
                        .and(filterDetailArea(detailArea))
                        .and(filterCloseExclude(closeExclude)))
                .fetch();
    }


    private BooleanExpression filterCloseExclude(Boolean status){
        if (!status){
            return null;
        }
        return theme.openStatus.isTrue();
    }

    private BooleanExpression filterAreaType(AreaType areaType){
        if (areaType == null){
            return null;
        }
        return theme.areaType.eq(areaType);
    }

    private BooleanExpression filterDetailArea(String areaName){
        if (StringUtils.isEmpty(areaName)){
            return null;
        }
        return theme.detailArea.eq(areaName);
    }


}
