package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DisabilityType {

    DISABLED("장애"),

    NORMAL("비장애");

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
