package com.kimleepark.thesilver.attend.presentation;


import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendType;
import com.kimleepark.thesilver.attend.dto.response.ResponseTypeAndAttend;
import com.kimleepark.thesilver.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AttendController {

    private final AttendService attendService;

    @GetMapping("/myAttend")
    public ResponseEntity<ResponseTypeAndAttend> getEmpAttend(@RequestParam final int month, @RequestParam final int year) throws ParseException {
        int empNo = 1;

        List<ResponseAttend> responseAttend = attendService.getEmpAttend(empNo,month,year);

        ResponseAttendType responseAttendType = ResponseAttendType.getAttendTypeCount(responseAttend);

        log.info("responseAttend : {}", responseAttend);
        log.info("responseAttendType : {}", responseAttendType);

        ResponseTypeAndAttend responseTypeAndAttend = ResponseTypeAndAttend.of(responseAttend,responseAttendType);

        return ResponseEntity.ok(responseTypeAndAttend);

    }

    @PostMapping("/enter")
    public ResponseEntity<Void> enter(){
        int empNo = 1;

        attendService.enterTimeSave(empNo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PutMapping("/leave")
    public ResponseEntity<Void> leave(){
        int empNo = 1;
        LocalDate today = LocalDate.now();

        log.info("today : {}",today);

        attendService.leaveTimeSave(empNo, today);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
