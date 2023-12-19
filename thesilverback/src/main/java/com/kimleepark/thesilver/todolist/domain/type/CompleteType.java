package com.kimleepark.thesilver.todolist.domain.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CompleteType {
    COMPLETE("complete"),

    INCOMPLETE("incomplete");

    private final String value;


    CompleteType(String value) {
        this.value = value;
    }


    @JsonValue
    public String getValue() {
        return value;
    }
}
