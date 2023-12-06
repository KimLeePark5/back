package com.kimleepark.thesilver.customer.dto.request;

import lombok.Data;

@Data
public class CustomerSearchRequest {

    private final String searchType;
    private final String searchContent;



    public static CustomerSearchRequest of (String searchType, String searchContent) {
        return new CustomerSearchRequest(searchType, searchContent);
    }
}
