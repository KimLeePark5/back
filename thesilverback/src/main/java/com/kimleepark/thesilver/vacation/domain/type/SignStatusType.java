package com.kimleepark.thesilver.vacation.domain.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignStatusType {

    PASS("결재완료"),
    PROCEED("상신"),
    RETURN("반려");

    private final String value;

    SignStatusType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static SignStatusType from(String value) {
        for (SignStatusType signStatus : SignStatusType.values()) {
            if (signStatus.getValue().equals(value)) {
                return signStatus;
            }
        }
        return null;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
