package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum WorkingType {

    WORK("work"),

    TIMEOFF("timeOff");

    private final String value;

    WorkingType(String value){this.value=value;}

    @JsonCreator
    public static WorkingType from(String value){
        for(WorkingType workingStatus : WorkingType.values()){
            if(workingStatus.getValue().equals(value)){
                return workingStatus;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
