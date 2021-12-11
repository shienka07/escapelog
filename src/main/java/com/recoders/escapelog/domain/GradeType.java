package com.recoders.escapelog.domain;

public enum GradeType {
    LEVEL1(1,"문하생"), LEVEL2(2,"탐정"), LEVEL3(3,"명탐정"), LEVEL4(4,"추리작가");

    private final int value;
    private final String title;

    GradeType(int value, String title){
        this.value = value;
        this.title = title;
    }

    public int getValue(){return value;}
    public String getTitle(){return title;}

}
