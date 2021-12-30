package com.recoders.escapelog.domain;

public enum AreaGroup{
    SEOUL("서울"), GYEONGGI("경기도"), INCHEON("인천"), GANGWON("강원도"),
    CHUNGCHEONG("충청도"), DAEJEON("대전"), SEJONG("세종"),
    BUSAN("부산"),ULSAN("울산"),DAEGU("대구"), GYEONGSANG("경상도"),
    JEOLLA("전라도"), GWANGJU("광주"), JEJU("제주도");

    private final String krName;
    public String getKrName(){
        return krName;
    }
    private AreaGroup(String name){
        krName = name;
    }

}
