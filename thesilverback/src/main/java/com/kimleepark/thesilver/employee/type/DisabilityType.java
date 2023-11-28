package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisabilityType {

    DISABLED("disabled"),

    NORMAL("normal");

    private final String value;

    DisabilityType(String value){this.value=value;}

    @JsonCreator
    public static DisabilityType from(String value){
        for(DisabilityType disability : DisabilityType.values()){
            if(disability.getValue().equals(value)){
                return disability;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }


}
