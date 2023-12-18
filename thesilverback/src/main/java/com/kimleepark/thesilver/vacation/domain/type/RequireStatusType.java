package com.kimleepark.thesilver.vacation.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequireStatusType {

    PASS("결재완료"),
    PROCEED("상신중"),
    RETURN("반려"),
    CANCEL("취소");

    private final String value;

    RequireStatusType(String value) { this.value = value; }

    @JsonCreator
    public static RequireStatusType from(String value) {
        for(RequireStatusType reqStatus : RequireStatusType.values()) {
            if(reqStatus.getValue().equals(value)) {
                return reqStatus;
            }
        }
        return null;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}