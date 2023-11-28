package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MarriageType {

    MARRIAGE("결혼"),

    SINGLE("미혼");

    private final String value;

    MarriageType(String value){this.value=value;}

    @JsonCreator
    public static MarriageType from(String value){
        for(MarriageType marriage : MarriageType.values()){
            if(marriage.getValue().equals(value)){
                return marriage;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
