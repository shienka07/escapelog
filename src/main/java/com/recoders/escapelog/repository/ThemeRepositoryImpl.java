package com.recoders.escapelog.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.recoders.escapelog.domain.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recoders.escapelog.dto.ThemeRecodeDto;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.recoders.escapelog.domain.QTheme.theme;

@RequiredArgsConstructor
public class ThemeRepositoryImpl implements ThemeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QTheme theme = QTheme.theme;
    private final QRecode recode = QRecode.recode;

    @Override
    public List<ThemeRecodeDto> findAllTheme(Member member){

        List<ThemeRecodeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeRecodeDto.class,theme.no.as("themeNo"), theme.themeName, theme.shopName, theme.imageUrl,
                        theme.openStatus, recode.success))
                .from(theme)
                .leftJoin(recode).on(recode.member.no.eq(member.getNo()).and(theme.no.eq(recode.theme.no)))
                .groupBy(theme.no)
                .fetch();

        return list;

    }

    @Override
    public List<ThemeRecodeDto> searchTheme(Member member,String keyword,AreaType areaType, String detailArea, Boolean closeExclude){

        String searchKeyword = "%"+keyword+"%";

        List<ThemeRecodeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeRecodeDto.class,theme.no.as("themeNo"), theme.themeName, theme.shopName, theme.imageUrl,
                        theme.openStatus, recode.success))
                .from(theme)
                .where(theme.themeName.like(searchKeyword)
                        .or(theme.shopName.like(searchKeyword))
                        .and(filterAreaType(areaType))
                        .and(filterDetailArea(detailArea))
                        .and(filterCloseExclude(closeExclude)))
                .leftJoin(recode).on(recode.member.no.eq(member.getNo()).and(theme.no.eq(recode.theme.no)))
                .groupBy(theme.no)
                .fetch();

        return list;

    }

    @Override
    public List<ThemeRecodeDto> searchThemeStampExclude(Member member,String keyword,AreaType areaType, String detailArea, Boolean closeExclude){

        String searchKeyword = "%"+keyword+"%";

        List<ThemeRecodeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeRecodeDto.class,theme.no.as("themeNo"), theme.themeName, theme.shopName, theme.imageUrl,
                        theme.openStatus, recode.success))
                .from(theme)
                .where(theme.themeName.like(searchKeyword)
                        .or(theme.shopName.like(searchKeyword))
                        .and(filterAreaType(areaType))
                        .and(filterDetailArea(detailArea))
                        .and(filterCloseExclude(closeExclude)))
                .leftJoin(recode).on(recode.member.no.eq(member.getNo()).and(theme.no.eq(recode.theme.no)))
                .groupBy(theme.no)
                .having(recode.success.isNull())
                .fetch();

        return list;

    }

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
