package com.recoders.escapelog.domain;

public enum AreaType {
    SEOUL("서울"), GYEONGGI("경기"), INCHEON("인천"), GANGWON("강원"), CHUNGCHEONGBUK("충북"),
    CHUNGCHEONGNAM("충남"), DAEJEON("대전"), JEOLLABUK("전북"), JEOLLANAM("전남"), GWANGJU("광주"),
    GYEONGSANGBUK("경북"), GYEONGSANGNAM("경남"), DAEGU("대구"), BUSAN("부산"), JEJU("제주"),
    ULSAN("울산"),SEJONG("세종");

    private final  String krName;

    private AreaType(String name){
        krName = name;
    }

    public String getKrName(){
        return krName;
    }

    public static AreaType nameOf(String name){
        for(AreaType areas : AreaType.values()){
            if (areas.getKrName().equals(name)){
                return areas;
            }
        }
        return null;
    }

}
