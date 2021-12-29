package com.recoders.escapelog.dto;

import com.recoders.escapelog.domain.AreaGroup;
import lombok.Getter;

@Getter
public class MapAreaDto {

    String areaGroup;
    String areaKrName;
    int areaThemeNum;
    int visitNum;
    int successNum;
    int stampLevel;
    int requiredNum;
    double visitNumPercent;
    double successNumPercent;


    public MapAreaDto(AreaGroup areaGroup, int areaThemeNum,int visitNum, int successNum){

        this.areaGroup = areaGroup.name();
        this.areaKrName = areaGroup.getKrName();
        this.areaThemeNum = areaThemeNum;
        this.visitNum = visitNum;
        this.successNum = successNum;
        this.visitNumPercent = visitNum*areaThemeNum !=0 ? calculatePercentage(visitNum,areaThemeNum) : 0;
        this.successNumPercent = successNum*areaThemeNum !=0 ? calculatePercentage(successNum,areaThemeNum) : 0;
        setAreaStampLevel(this.visitNumPercent);
    }

    public void setAreaStampLevel(double visitNumPercent){
        if (visitNumPercent>=70){
            this.stampLevel = 3;
        }else if(visitNumPercent>=50){
            this.stampLevel = 2;
            this.requiredNum = (int) Math.ceil((0.7*areaThemeNum)-visitNum);
        }else if(visitNumPercent>=20){
            this.requiredNum = (int) Math.ceil((0.5*areaThemeNum)-visitNum);
            this.stampLevel = 1;
        }else{
            this.requiredNum = (int) Math.ceil((0.2*areaThemeNum)-visitNum);
            this.stampLevel = 0;
        }
    }

    public double calculatePercentage(int numerator, int denominator){
        double result = Double.parseDouble(String.format("%.2f",(double)numerator/denominator*100));
        return result;
    }

}

