package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum LeaveType {

    YES("퇴사"),

    NO("근무");

    private final String value;

    LeaveType(String value){this.value=value;}

    @JsonCreator
    public static LeaveType from(String value){
        for(LeaveType leaveType : LeaveType.values()){
            if(leaveType.getValue().equals(value)){
                return leaveType;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
