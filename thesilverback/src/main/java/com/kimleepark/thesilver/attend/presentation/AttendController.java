package com.kimleepark.thesilver.attend.presentation;


import com.kimleepark.thesilver.attend.dto.ResponseModifiedAttend;
import com.kimleepark.thesilver.attend.dto.request.RequestAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendAdminAndModifiedAttend;
import com.kimleepark.thesilver.attend.dto.response.ResponseAttendType;
import com.kimleepark.thesilver.attend.dto.response.ResponseTypeAndAttend;
import com.kimleepark.thesilver.attend.service.AttendService;
import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
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
import java.util.stream.Collectors;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
@Slf4j
public class AttendController {

    private final AttendService attendService;

    @GetMapping("/myAttend")
    public ResponseEntity<ResponseTypeAndAttend> getEmpAttend(@RequestParam final String month){
        int empNo = 1;
        List<ResponseAttend> responseAttend = attendService.getEmpAttend(empNo,month);

        ResponseAttendType responseAttendType = ResponseAttendType.getAttendTypeCount(responseAttend,(long)empNo);

        log.info("responseAttend : {}", responseAttend);
        log.info("responseAttendType : {}", responseAttendType);

        ResponseTypeAndAttend responseTypeAndAttend = ResponseTypeAndAttend.of(responseAttend,responseAttendType);

        return ResponseEntity.ok(responseTypeAndAttend);
    }

    @PostMapping("/enter")
    public ResponseEntity<Void> enter(){
        int empNo = 1;


        attendService.dupcheckToday(empNo);

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

        log.info("requestAttend : {}",requestAttend);
        log.info("attendNo : {}",attendNo);
        int empNo = 2; //수정자
        attendService.modifyAttend(empNo,attendNo,requestAttend);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getModifiedAttend/{modifiedNo}")
    public ResponseEntity<PagingResponse> getModifiedAttend(@RequestParam(defaultValue = "1") Integer page,@PathVariable int modifiedNo){

        Page<ResponseModifiedAttend> responseModifiedAttends = attendService.getModifiedAttend(page, modifiedNo);

        PagingButtonInfo pagingButton = Pagenation.getPagingButtonInfo(responseModifiedAttends);

        PagingResponse pagingResponse = PagingResponse.of(responseModifiedAttends, pagingButton);


        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/getAttendAdmin")
    public ResponseEntity<PagingResponse> getAttendAdmin(@RequestParam(defaultValue = "1") final Integer page, String month){
        ResponseAttendAdminAndModifiedAttend list = attendService.getAttendAdmin(page,month);

        PagingButtonInfo button = Pagenation.getPagingButtonInfo(list.getResponseAttendAdmin());

        PagingResponse pagingResponse = PagingResponse.of(list,button);

        return ResponseEntity.ok(pagingResponse);
    }

    @GetMapping("/getAttendAdminByEmpName")
    public ResponseEntity<PagingResponse> getAttendAdminByName(@RequestParam(defaultValue = "1") final Integer page, String month,String name){

        ResponseAttendAdminAndModifiedAttend list = attendService.getAttendAdminByName(page,month,name);
        PagingButtonInfo button = Pagenation.getPagingButtonInfo(list.getResponseAttendAdmin());
        PagingResponse pagingResponse = PagingResponse.of(list,button);

        return ResponseEntity.ok(pagingResponse);
    }

}
