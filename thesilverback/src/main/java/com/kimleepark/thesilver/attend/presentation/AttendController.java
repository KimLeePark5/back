package com.kimleepark.thesilver.attend.presentation;


import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendType;
import com.kimleepark.thesilver.attend.dto.response.ResponseTypeAndAttend;
import com.kimleepark.thesilver.attend.service.AttendService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AttendController {

    private final AttendService attendService;

    @GetMapping("/myAttend")
    public ResponseEntity<ResponseTypeAndAttend> getEmpAttend(@RequestParam final String month) throws ParseException {
        int empNo = 1;
        List<ResponseAttend> responseAttend = attendService.getEmpAttend(empNo,month);

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

    @GetMapping("/todayMyAttend")
    public ResponseEntity<ResponseAttend> todayAttend(){
        int empNo = 1;
        LocalDate today = LocalDate.now();
        ResponseAttend responseAttend = attendService.getTodayAttend(empNo,today);
        return ResponseEntity.ok(responseAttend);
    }

    @PutMapping("/modifyAttend/{attendNo}")
    public ResponseEntity<Void> modifyAttend(@PathVariable final int attendNo, @RequestBody RequestAttend requestAttend){
                                                                                // Requestparam
        log.info("requestAttend : {}",requestAttend);
        log.info("attendNo : {}",attendNo);
        int empNo = 2; //수정자
        attendService.modifyAttend(empNo,attendNo,requestAttend);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getMdofiedAttend")
    public ResponseEntity<Page<ResponseModifiedAttend>> getModifiedAttend(@RequestParam(defaultValue = "1") Integer page){
        Page<ResponseModifiedAttend> responseModifiedAttends = attendService.getModifiedAttend(page);



        return null;
    }

}
