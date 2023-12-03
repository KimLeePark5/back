package com.kimleepark.thesilver.Program.presentation;


import com.kimleepark.thesilver.Program.domain.Teacher;
import com.kimleepark.thesilver.Program.dto.request.ProgramCreateRequest;
import com.kimleepark.thesilver.Program.dto.request.ProgramUpdateRequest;
import com.kimleepark.thesilver.Program.dto.response.CustomerProgramResponse;
import com.kimleepark.thesilver.Program.dto.response.CustomerProgramsResponse;
import com.kimleepark.thesilver.Program.service.ProgramService;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.common.paging.Pagenation;
import com.kimleepark.thesilver.common.paging.PagingButtonInfo;
import com.kimleepark.thesilver.common.paging.PagingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProgramController {

    private final ProgramService programService;

    // 1. 프로그램 목록 조회 - 페이징 (직원)
    @GetMapping("/programs")
    public ResponseEntity<PagingResponse> getCustomerPrograms(@RequestParam(defaultValue = "1") final Integer page) {

        final Page<CustomerProgramsResponse> programs = programService.getCustomerPrograms(page);
        final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(programs);
        final PagingResponse pagingResponse = PagingResponse.of(programs.getContent(), pagingButtonInfo);

        // 테스트 ~~
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("프린시팔 getPrincipal {}", authentication.getPrincipal());
        log.info("프린시팔 getCredentials {}", authentication.getCredentials());
        log.info("프린시팔 getAuthorities {}", authentication.getAuthorities());
        log.info("프린시팔 getDetails {}", authentication.getDetails());
        log.info("프린시팔 getName {}", authentication.getName());
        log.info("프린시팔 getClass {}", authentication.getClass());


        return ResponseEntity.ok(pagingResponse);
    }

    // 2. 프로그램 목록 조회 - 프로그램명 검색 기준, 페이징 (직원, 관리자)
    @GetMapping("/programs/search")
    public ResponseEntity<PagingResponse> getCustomerProgramsByCategory(
            @RequestParam(defaultValue = "1") final Integer page, @RequestParam final String categoryName) {

        // 요청 파라미터 확인
        System.out.println("요청 파라미터 확인 Received request to get customer programs by category. Page: " + page + ", Category: " + categoryName);

        try {
            // 데이터 받아오기
            final Page<CustomerProgramsResponse> programs = programService.getCustomerProgramsByCategory(page, categoryName);

            // 프린트 추가: 받아온 데이터 확인
            System.out.println("받아온 데이터 확인 Received " + programs.getContent().size() + " programs for category '" + categoryName + "'");

            // 페이지 정보 생성
            final PagingButtonInfo pagingButtonInfo = Pagenation.getPagingButtonInfo(programs);

            // 응답 생성
            final PagingResponse pagingResponse = PagingResponse.of(programs.getContent(), pagingButtonInfo);

            // 프린트 추가: 성공 응답 확인
            System.out.println("성공 응답 확인 Returning programs response for category '" + categoryName + "'");

            return ResponseEntity.ok(pagingResponse);

        } catch (Exception e) {
            // 프린트 추가: 예외 발생 시 에러 출력
            System.err.println("예외 발생 시 에러 출력 Error while processing request for customer programs by category '" + categoryName + "': " + e.getMessage());

            // 에러 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 3. 프로그램 상세 조회 - category_code 로 프로그램 1개 조회(고객, 관리자)
    @GetMapping("/programs/{categoryCode}")
    public ResponseEntity<CustomerProgramResponse> getCustomerProgram(@PathVariable final Long categoryCode) {

        final CustomerProgramResponse customerProgramResponse = programService.getCustomerProgram(categoryCode);

        return ResponseEntity.ok(customerProgramResponse);
    }

     //4. 프로그램 등록 (관리자)
     @PostMapping("/programs")
     public ResponseEntity<Void> save(@RequestPart @Valid ProgramCreateRequest programRequest,
                                      @RequestPart MultipartFile teacherImg) {

         System.out.println("programRequest" + programRequest);
         // ProgramService의 save 메서드 호출
         Long programCode = programService.save(teacherImg, programRequest);

         // 프로그램이 성공적으로 저장되면 해당 프로그램의 URI를 반환
         return ResponseEntity.created(URI.create("/programs-management/" + programCode)).build();
     }

    // 5. 프로그램 수정(관리자)
    @PutMapping("/programs/{programCode}")
    public ResponseEntity<Void> update(@PathVariable final Long programCode,
                                       @RequestPart @Valid final ProgramUpdateRequest programRequest,
                                       @RequestPart(required = false) final MultipartFile teacherImg) {
        try {
            // ProgramService의 update 메서드 호출
            programService.update(programCode, teacherImg, programRequest);

            // 프로그램이 성공적으로 수정되면 해당 프로그램의 URI를 반환
            return ResponseEntity.created(URI.create("/programs-management/" + programCode)).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            // 프로그램 수정 중 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 프로그램 삭제(관리자)
    @DeleteMapping("/programs/{programCode}")
    public ResponseEntity<Void> delete(@PathVariable final Long programCode) {

        try {
            programService.delete(programCode);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            // 프로그램을 찾을 수 없는 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            // 프로그램 삭제 중 오류 발생 시 500 Internal Server Error 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}