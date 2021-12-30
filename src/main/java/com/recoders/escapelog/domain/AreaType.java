package com.recoders.escapelog.domain;

import java.util.ArrayList;
import java.util.List;

public enum AreaType {

    SEOUL("서울",AreaGroup.SEOUL), GYEONGGI("경기",AreaGroup.GYEONGGI), INCHEON("인천",AreaGroup.INCHEON), GANGWON("강원",AreaGroup.GANGWON),
    CHUNGCHEONGBUK("충북",AreaGroup.CHUNGCHEONG),CHUNGCHEONGNAM("충남",AreaGroup.CHUNGCHEONG), DAEJEON("대전",AreaGroup.DAEJEON),SEJONG("세종",AreaGroup.SEJONG),
    BUSAN("부산",AreaGroup.BUSAN),ULSAN("울산",AreaGroup.ULSAN),DAEGU("대구",AreaGroup.DAEGU), GYEONGSANGBUK("경북",AreaGroup.GYEONGSANG), GYEONGSANGNAM("경남",AreaGroup.GYEONGSANG),
    JEOLLABUK("전북",AreaGroup.JEOLLA), JEOLLANAM("전남",AreaGroup.JEOLLA), GWANGJU("광주",AreaGroup.GWANGJU), JEJU("제주",AreaGroup.JEJU);

    private final  String krName;
    private final AreaGroup areaGroup;

    private AreaType(String name,AreaGroup groupName){
        krName = name;
        this.areaGroup = groupName;
    }

    public String getKrName(){
        return krName;
    }
    public boolean isInGroup(AreaGroup group){
        return this.areaGroup == group;
    }

    public static AreaType nameOf(String name){
        for(AreaType areas : AreaType.values()){
            if (areas.getKrName().equals(name)){
                return areas;
            }
        }
        return null;
    }

    public static List<String> getGroupList(AreaGroup group){
        List<String> areaTypeList = new ArrayList<>();
        for(AreaType areas : AreaType.values()){
            if (areas.isInGroup(group)){
                areaTypeList.add(areas.name());
            }
        }
        return areaTypeList;
    }
}
