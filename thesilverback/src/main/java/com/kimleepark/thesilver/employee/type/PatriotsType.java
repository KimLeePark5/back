package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PatriotsType {
    PATRIOTS("patriots"),

    NORMAL("normal");

    private final String value;

    PatriotsType(String value){this.value=value;}

    @JsonCreator
    public static PatriotsType from(String value){
        for(PatriotsType patriots : PatriotsType.values()){
            if(patriots.getValue().equals(value)){
                return patriots;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
