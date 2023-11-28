package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RankUpDownType {

    UP("진급"),

    DOWN("강등");

    private final String value;

    RankUpDownType(String value){this.value=value;}

    @JsonCreator
    public static RankUpDownType from(String value){
        for(RankUpDownType upDown : RankUpDownType.values()){
            if(upDown.getValue().equals(value)){
                return upDown;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}

