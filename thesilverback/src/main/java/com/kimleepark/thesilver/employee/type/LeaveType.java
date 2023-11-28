package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {

    YES("yes"),

    NO("no");

    private final String value;

    LeaveType(String value){this.value=value;}

    @JsonCreator
    public static LeaveType from(String value){
        for(LeaveType leave : LeaveType.values()){
            if(leave.getValue().equals(value)){
                return leave;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
