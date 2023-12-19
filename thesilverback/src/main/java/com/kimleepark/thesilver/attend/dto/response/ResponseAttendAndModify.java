package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class ResponseAttendAndModify {

    private final List<ResponseModifiedAttend> responseModifiedAttends;
    private final Page<ResponseAttendAdminTwo> responseAttendAdminTwos;


    public static ResponseAttendAndModify of(List<ResponseModifiedAttend> responseModifiedAttends, Page<ResponseAttendAdminTwo> two) {

        return new ResponseAttendAndModify(responseModifiedAttends, two);
    }
}
