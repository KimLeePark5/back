package com.kimleepark.thesilver.attend.dto.response;

import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ResponseAttendAdminAndModifiedAttend {

    private final Page<ResponseAttendAdmin> responseAttendAdmin;
    private final List<ResponseModifiedAttend> responseModifiedAttend;
    private final Page<ResponseAttendType> responseAttendTypes;
    public static ResponseAttendAdminAndModifiedAttend of(Page<ResponseAttendAdmin> responseAttendAdminList, List<ResponseModifiedAttend> responseModifiedAttends, Page<ResponseAttendType> responseAttendTypes) {
        return new ResponseAttendAdminAndModifiedAttend(responseAttendAdminList,responseModifiedAttends,responseAttendTypes);
    }
}
