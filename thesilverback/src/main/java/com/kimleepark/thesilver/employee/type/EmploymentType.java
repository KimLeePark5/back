package com.kimleepark.thesilver.employee.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EmploymentType {
    NEW("신입"),

    VETERAN("경력");

    private final String value;

    EmploymentType(String value){this.value=value;}

    @JsonCreator
    public static EmploymentType from(String value){
        for(EmploymentType employmentType : EmploymentType.values()){
            if(employmentType.getValue().equals(value)){
                return employmentType;
            }
        }
        return null;
    }

    @JsonValue
    private String getValue() {
        return value;
    }
}
