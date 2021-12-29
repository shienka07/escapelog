package com.recoders.escapelog.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MapDto {

    int totalThemeNum;
    int totalVisitNum;
    int totalSuccessNum;
    double totalVisitPercent;
    double totalSuccessPercent;

    List<MapAreaDto> areaInfoList;

    public MapDto(int totalThemeNum,int totalVisitNum, int totalSuccessNum){
        this.totalThemeNum = totalThemeNum;
        this.totalVisitNum = totalVisitNum;
        this.totalSuccessNum = totalSuccessNum;
        this.totalVisitPercent = totalVisitNum*totalThemeNum != 0  ? calculatePercentage(totalVisitNum, totalThemeNum) : 0;
        this.totalSuccessPercent = totalSuccessNum*totalThemeNum != 0 ? calculatePercentage(totalSuccessNum, totalThemeNum) : 0;

    }

    public double calculatePercentage(int numerator, int denominator){
        double result = Double.parseDouble(String.format("%.2f",(double)numerator/denominator*100));
        return result;
    }



}

