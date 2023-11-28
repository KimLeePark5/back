package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GenderType {

    MEN("men"),

    WOMAN("woman");

    private final String value;

    GenderType(String value){this.value=value;}

    @JsonCreator
    public static GenderType from(String value){
        for(GenderType gender : GenderType.values()){
            if(gender.getValue().equals(value)){
                return gender;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
