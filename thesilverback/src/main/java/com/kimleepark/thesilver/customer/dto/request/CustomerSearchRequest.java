package com.kimleepark.thesilver.customer.dto.request;

import lombok.Data;

@Data
public class CustomerSearchRequest {

    private final String searchType;
    private final String searchContent;
    private final Boolean activeCheck;



    public static CustomerSearchRequest of (String searchType, String searchContent, String searchActiveCheck) {
        Boolean activeCheck = searchActiveCheck.equals("true");

        return new CustomerSearchRequest(searchType, searchContent, activeCheck);
    }
}
