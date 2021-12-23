package com.recoders.escapelog.repository;

import com.querydsl.core.types.Projections;
import com.recoders.escapelog.domain.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.recoders.escapelog.dto.ThemeDto;
import lombok.RequiredArgsConstructor;
import org.thymeleaf.util.StringUtils;

import java.util.List;


@RequiredArgsConstructor
public class ThemeRepositoryImpl implements ThemeRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QTheme theme = QTheme.theme;
    private final QRecode recode = QRecode.recode;

    @Override
    public List<ThemeDto> findAllTheme(Member member){

        List<ThemeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeDto.class,theme.no, theme.themeName, theme.shopName, theme.imageUrl,
                        theme.openStatus, recode.success))
                .from(theme)
                .leftJoin(recode).on(recode.member.no.eq(member.getNo()).and(theme.no.eq(recode.theme.no)))
                .groupBy(theme.no)
                .fetch();

        return list;

    }

    @Override
    public List<ThemeDto> searchTheme(Member member,String keyword,AreaType areaType, String detailArea, Boolean closeExclude){

        String searchKeyword = "%"+keyword+"%";

        List<ThemeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeDto.class,theme.no, theme.themeName, theme.shopName, theme.imageUrl,
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
    public List<ThemeDto> searchThemeStampExclude(Member member,String keyword,AreaType areaType, String detailArea, Boolean closeExclude){

        String searchKeyword = "%"+keyword+"%";

        List<ThemeDto> list = queryFactory
                .selectDistinct(Projections.bean(ThemeDto.class,theme.no, theme.themeName, theme.shopName, theme.imageUrl,
                        theme.openStatus, recode.success))
                .from(theme)
                .where(theme.themeName.like(searchKeyword)
                        .or(theme.shopName.like(searchKeyword))
                        .and(filterAreaType(areaType))
                        .and(filterDetailArea(detailArea))
                        .and(filterCloseExclude(closeExclude)))
                .leftJoin(recode).on(recode.member.no.eq(member.getNo()).and(theme.no.eq(recode.theme.no)))
                .groupBy(theme.no)
                .having(recode.success.isNull().or(recode.success.isFalse()))
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
