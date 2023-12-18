package com.kimleepark.thesilver.board.journal.presentation;


import com.kimleepark.thesilver.board.journal.dto.request.JournalCreateRequest;
import com.kimleepark.thesilver.board.journal.dto.request.JournalUpdateRequest;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalResponse;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalsResponse;
import com.kimleepark.thesilver.board.journal.service.JournalService;
import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.repository.ProgramRepository;
import com.kimleepark.thesilver.board.program.dto.request.ProgramCreateRequest;
import com.kimleepark.thesilver.board.program.dto.request.ProgramUpdateRequest;
import com.kimleepark.thesilver.board.program.dto.response.CustomerProgramResponse;
import com.kimleepark.thesilver.board.program.dto.response.CustomerProgramsResponse;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.jwt.CustomUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_PROGRAM_CODE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class JournalController {

    private final JournalService journalService;
    private final ProgramRepository programRepository;

    // 1. 프로그램 일지 목록 조회
    @GetMapping("/journals")
    public ResponseEntity<PagingResponse> getCustomerProgramJournals(@RequestParam(defaultValue = "1") final Integer page) {

        final Page<CustomerJournalsResponse> journals = journalService.getCustomerProgramJournals(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(journals);
        final PagingResponse pagingResponse = PagingResponse.of(journals.getContent(), pagingButtonInfo);

        return ResponseEntity.ok(pagingResponse);
    }

    // 2. 일지 목록 조회 - 다중 검색 기준, 페이징 (직원, 프로그램 카테고리, 참관 일자)
//    @GetMapping("/journals/search")
//    public ResponseEntity<PagingResponse> getJournalsByMultipleCriteria(
//            @RequestParam(defaultValue = "1") final Integer page,
//            @RequestParam(required = false) final String employeeCode,
//            @RequestParam(required = false) final Long programCategoryCode,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate observation) {
//
//        // 다중 검색 기준에 따라 일지를 검색하여 페이징된 결과를 가져옵니다.
//        final Page<CustomerJournalsResponse> journals = journalService.getJournalsByMultipleCriteria(
//                page, employeeCode, programCategoryCode, observation);
//
//        // 페이징된 결과를 PagingResponse로 변환합니다.
//        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(journals);
//        final PagingResponse pagingResponse = PagingResponse.of(journals.getContent(), pagingButtonInfo);
//
//        // 생성된 PagingResponse를 ResponseEntity로 감싸서 반환합니다.
//        return ResponseEntity.ok(pagingResponse);
//    }
    // 2. 일지 목록 조회 - 다중 검색 기준, 페이징 (카테고리명, 직원명, 참관 일자)
    @GetMapping("/journals/search")
    public ResponseEntity<PagingResponse> getJournalsByMultipleCriteria(
            @RequestParam(defaultValue = "1") final Integer page,
            @RequestParam(required = false) final String categoryName,
            @RequestParam(required = false) final String employeeName,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate observation) {

        // 다중 검색 기준에 따라 일지를 검색하여 페이징된 결과를 가져옵니다.
        final Page<CustomerJournalsResponse> journals = journalService.getJournalsByMultipleCriteria(
                page, categoryName, employeeName, observation);

        // 페이징된 결과를 PagingResponse로 변환합니다.
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(journals);
        final PagingResponse pagingResponse = PagingResponse.of(journals.getContent(), pagingButtonInfo);

        // 생성된 PagingResponse를 ResponseEntity로 감싸서 반환합니다.
        return ResponseEntity.ok(pagingResponse);
    }

    //2-1. 다중 검색 셀렉트 바 (직원 이름)
    @GetMapping("/journals/search/employeeNames")
    public ResponseEntity<List<String>> getEmployeeName() {
        List<String> employeeName = journalService.getEmployeeName();
        return ResponseEntity.ok(employeeName);
    }

    //2-2. 다중 검색 셀렉트 바 (카테고리 이름)
    @GetMapping("/journals/search/categoryNames")
    public ResponseEntity<List<String>> getCategoryName() {
        List<String> categoryName = journalService.getCategoryName();
        return ResponseEntity.ok(categoryName);
    }


    // 3. 일지 상세 조회 - journalCode 로 프로그램 1개 조회(직원, 관리자)
    @GetMapping("/journals/{journalCode}")
    public ResponseEntity<CustomerJournalResponse> getCustomerJournal(@PathVariable final Long journalCode) {
        // 요청된 journalCode 값 로깅
        log.info("Requested journal with code: {}", journalCode);

        // 서비스를 호출하여 일지를 조회합니다.
        try {
            CustomerJournalResponse response = journalService.getCustomerJournal(journalCode);

            // 조회된 결과를 ResponseEntity로 감싸서 반환합니다.
            log.info("Journal response: {}", response);
            return ResponseEntity.ok(response);

        } catch (NotFoundException ex) {
            // NotFoundException이 발생하면 해당 일지가 없다는 응답을 반환합니다.
            log.error("Journal not found for code: {}", journalCode);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception ex) {
            // 그 외의 예외 발생 시 서버 오류 응답을 반환합니다.
            log.error("Error while processing request for journal with code: {}", journalCode, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 4. 일지 등록 - (참관한 직원, 관리자)
    @PostMapping("/journals")  // 프로그램 스케줄에 등록된 직원만 해당 프로그램 종료시간 이후에 일지를 작성할수 있게 조건 써야함. 직원코드 와 일치하는 직원이름이 자동 조회.
    public ResponseEntity<Void> save(@RequestPart @Valid JournalCreateRequest journalRequest,
                                     @RequestPart MultipartFile journalImg) {

        try {
            // 프로그램 정보 가져오기
            Program program = (Program) programRepository.findByCategoryCategoryNameAndRound(journalRequest.getCategoryName(), journalRequest.getRound())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

            System.out.println("프로그램 카테고리 정보 조회: " + program.getCode());

            // 프로그램의 시작일, 종료일, 요일, 시작시간, 종료시간 확인
            LocalDate programStartDate = LocalDate.from(LocalDate.from(program.getStartDate()));
            LocalDate programEndDate = LocalDate.from(LocalDate.from(program.getEndDate()));
            String programDay = program.getDay();
            LocalTime programStartTime = program.getStartTime();
            LocalTime programEndTime = program.getEndTime();
            String programRound = program.getRound();

            // 직원의 참관 날짜 가져오기
            LocalDate observation = journalRequest.getObservation();

            System.out.println("프로그램 시작일: " + programStartDate);
            System.out.println("프로그램 종료일: " + programEndDate);
            System.out.println("프로그램 요일: " + programDay);
            System.out.println("프로그램 시작 시간: " + programStartTime);
            System.out.println("프로그램 종료 시간: " + programEndTime);
            System.out.println("참관 날짜: " + observation);
            System.out.println("회 차 : " + programRound);

            // 프로그램에 해당하는 날짜인지 확인
            if (!isJournalDateValid(programStartDate, programEndDate, programDay, observation)) {
                // 프로그램의 날짜에 맞지 않으면 오류 응답
                System.out.println("프로그램의 날짜에 맞지 않습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            System.out.println("프로그램 날짜 확인 완료. 일지 서비스 호출...");

            // 프로그램에 해당하는 강사 이름 조회
            String teacherName = program.getTeacher().getTeacherName();

            // journalRequest에 강사 이름 설정
            journalRequest.setTeacherName(teacherName);

            // journalRequest에 startTime 및 endTime 설정
            journalRequest.setStartTime(programStartTime);
            journalRequest.setEndTime(programEndTime);
            System.out.println("강사이름 : " + teacherName);
            System.out.println("시작시간 : " + programStartTime);
            System.out.println("종료시간 : " + programEndTime);
//----------------------------------------------------------------
            // 직원 이름 조회 (현재 로그인한 직원)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // principal(주체) 객체 가져오기
            Object principal = authentication.getPrincipal();

            // principal이 Employee 객체인지 확인
            if (principal instanceof Employee) {
                // Employee로 캐스팅
                Employee employee = (Employee) principal;

                // Employee 객체에서 employeeName 가져오기
                String employeeName = employee.getEmployeeName();

                // journalRequest에 직원 이름 설정
                journalRequest.setEmployeeName(employeeName);

                // 일치하지 않으면 예외 처리
                if (!employee.getEmployeeName().equals(journalRequest.getEmployeeName())) {
                    throw new UnsupportedOperationException("현재 로그인한 직원의 이름과 요청한 일지에 등록된 직원의 이름이 일치하지 않습니다.");
                }
            }
//-------------------------------------------------------------------
            // 프로그램 정보가 정상적으로 조회되었으므로 일지 서비스 호출
            Long journalCode = journalService.save(journalImg, journalRequest);

            System.out.println("일지가 성공적으로 저장되었습니다. 일지 코드: " + journalCode);

            // 일지가 성공적으로 저장되면 해당 일지의 URI를 반환
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("/{journalCode}")
                    .buildAndExpand(journalCode).toUri();

            return ResponseEntity.created(location).build();
        } catch (NotFoundException e) {
            log.error("프로그램 또는 일지를 찾을 수 없습니다.", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("일지 저장 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isJournalDateValid(LocalDate programStartDate, LocalDate programEndDate, String programDay, LocalDate observation) {
        System.out.println("Program Day: " + programDay); // 디버깅
        // 프로그램의 시작일, 종료일 사이에 journalObserve가 있는지 확인
        boolean isWithinDateRange = !observation.isBefore(programStartDate) && !observation.isAfter(programEndDate);

        // 한글 요일을 영어 요일로 변환
        DayOfWeek dayOfWeek;
        switch (programDay) {
            case "월":
                dayOfWeek = DayOfWeek.MONDAY;
                break;
            case "화":
                dayOfWeek = DayOfWeek.TUESDAY;
                break;
            case "수":
                dayOfWeek = DayOfWeek.WEDNESDAY;
                break;
            case "목":
                dayOfWeek = DayOfWeek.THURSDAY;
                break;
            case "금":
                dayOfWeek = DayOfWeek.FRIDAY;
                break;
            default:
                throw new IllegalArgumentException("Invalid day of week: " + programDay);
        }
        // 일지의 날짜의 요일을 확인
        DayOfWeek observationDayOfWeek = observation.getDayOfWeek();

        // 프로그램의 요일과 일지의 요일이 일치하는지 확인
        return isWithinDateRange && observationDayOfWeek == dayOfWeek;
    }

    // 5. 일지 수정
    @PutMapping("/journals/{journalCode}") // 프로그램 일지에 등록된 직원코드와 현재 로그인한 직원 코드가 일치해야 수정 가능하게 조건 써야함. 뷰에 보일떄는 이름으로 보이기.(관리자는 다 가능)
    public ResponseEntity<Void> update(@PathVariable final Long journalCode,
                                       @RequestPart(required = false) final MultipartFile journalImg,
                                       @RequestPart @Valid final JournalUpdateRequest journalRequest
    ) {
        try {
            // 현재 로그인한 사용자의 권한을 확인
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            boolean isCenterDirectorOrTeamLeader = isUserCenterDirectorOrTeamLeader(authentication);

            // journalService의 update 메서드 호출
            journalService.update(journalCode, journalImg, journalRequest, isCenterDirectorOrTeamLeader);

            // 일지가 성공적으로 수정되면 해당 일지 첨부파일 URI를 반환
            return ResponseEntity.created(URI.create(String.valueOf(journalCode))).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            //일지 수정 중 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private boolean isUserCenterDirectorOrTeamLeader(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // 모든 권한을 허용하도록 수정
            return userDetails.getAuthorities().stream().anyMatch(authority -> true);
        }
        return false;
    }


    // 6. 일지 삭제(등록한 직원, 관리자) //사용자가 아무 일지나 다 삭제함;;
    @DeleteMapping("/journals/{journalCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long journalCode) {

        try {
            journalService.delete(journalCode);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            // 일지를 찾을 수 없는 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            // 일지 삭제 중 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
