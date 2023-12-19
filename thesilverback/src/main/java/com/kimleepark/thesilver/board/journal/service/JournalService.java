package com.kimleepark.thesilver.board.journal.service;

import com.kimleepark.thesilver.attachment.Attachment;
import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.board.journal.domain.repository.JournalRepository;
import com.kimleepark.thesilver.board.journal.dto.request.JournalCreateRequest;
import com.kimleepark.thesilver.board.journal.dto.request.JournalUpdateRequest;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalResponse;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalsResponse;
import com.kimleepark.thesilver.board.participant.Participant;
import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.Teacher;
import com.kimleepark.thesilver.board.program.domain.repository.ProgramRepository;
import com.kimleepark.thesilver.common.exception.CustomException;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.common.exception.type.ExceptionCode;
import com.kimleepark.thesilver.common.util.FileUploadUtils;
import com.kimleepark.thesilver.customer.domain.Customer;
import com.kimleepark.thesilver.customer.domain.repository.CustomerRepository;
import com.kimleepark.thesilver.employee.Employee;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class JournalService {

    private final JournalRepository journalRepository;
    private final ProgramRepository programRepository;
    private final EmployeeRepository employeeRepository; // 추가
    private final CustomerRepository customerRepository;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;


    private Pageable getPageable(Integer page) {return PageRequest.of(page - 1, 10, Sort.by("journalCode").descending());}

    // 1. 일지 목록 조회
    @Transactional(readOnly = true)
    public Page<CustomerJournalsResponse> getCustomerProgramJournals(Integer page) {

        Page<Journal> journals = journalRepository.findAllBy(getPageable(page));

        return journals.map(journal -> CustomerJournalsResponse.from(journal));
    }

    // 2. 일지 목록 조회 - 다중 검색 기준, 페이징 (직원, 프로그램 카테고리, 참관 일자)
//    @Transactional(readOnly = true)
//    public Page<CustomerJournalsResponse> getJournalsByMultipleCriteria(
//            Integer page, String employeeCode, Long programCategoryCode, LocalDate observation) {
//
//        // 다중 검색 기준에 따라 동적으로 쿼리를 생성
//        Specification<Journal> spec = Specification.where(null);
//
//        if (StringUtils.hasText(employeeCode)) {
//            spec = spec.and((root, query, builder) -> {
//                // 직원 코드 검색 조건을 추가
//                System.out.println("Adding employeeCode condition: " + employeeCode);
//                return builder.equal(root.get("employee").get("employeeCode"), employeeCode);
//            });
//        }
//
//        if (programCategoryCode != null) {
//            spec = spec.and((root, query, builder) -> {
//                // 프로그램 카테고리 코드 검색 조건을 추가
//                System.out.println("Adding programCategoryCode condition: " + programCategoryCode);
//                return builder.equal(root.get("program").get("category").get("categoryCode"), programCategoryCode);
//            });
//        }
//
//        if (observation != null) {
//            spec = spec.and((root, query, builder) -> {
//                // 참관 일자 검색 조건을 추가
//                System.out.println("Adding observation condition: " + observation);
//                return builder.equal(root.get("observation"), observation);
//            });
//        }
//
//        Page<Journal> journals = journalRepository.findAll(spec, getPageable(page));
//
//        if (journals.isEmpty()) {
//            throw new CustomException(ExceptionCode.NOT_FOUND_MULTIPLE_LOOKUPS);
//        }
//        return journals.map(journal -> CustomerJournalsResponse.from(journal));
//    }
    // 2. 일지 목록 조회 - 다중 검색 기준, 페이징 (카테고리명, 직원명, 참관 일자)
    @Transactional(readOnly = true)
    public Page<CustomerJournalsResponse> getJournalsByMultipleCriteria(
            Integer page, String categoryName, String employeeName, LocalDate observation) {

        // 다중 검색 기준에 따라 동적으로 쿼리를 생성
        Specification<Journal> spec = Specification.where(null);

        if (categoryName != null) {
            spec = spec.and((root, query, builder) -> {
                // 카테고리명 검색 조건을 추가
                System.out.println("Adding categoryName condition: " + categoryName);
                return builder.equal(root.get("program").get("category").get("categoryName"), categoryName);
            });
        }

        if (StringUtils.hasText(employeeName)) {
            spec = spec.and((root, query, builder) -> {
                // 직원명 검색 조건을 추가
                System.out.println("Adding employeeName condition: " + employeeName);
                return builder.equal(root.get("employee").get("employeeName"), employeeName);
            });
        }

        if (observation != null) {
            spec = spec.and((root, query, builder) -> {
                // 참관 일자 검색 조건을 추가
                System.out.println("Adding observation condition: " + observation);
                return builder.equal(root.get("observation"), observation);
            });
        }

        Page<Journal> journals = journalRepository.findAll(spec, getPageable(page));

        if (journals.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_MULTIPLE_LOOKUPS);
        }
        return journals.map(journal -> CustomerJournalsResponse.from(journal));
    }

    // 3. 일지 상세 조회 - journalCode 로 프로그램 1개 조회(고객, 관리자)
    @Transactional(readOnly = true)
    public CustomerJournalResponse getCustomerJournal(final Long journalCode) {
        // journalCode로 일지를 조회합니다.
        Journal journal = journalRepository.findById(journalCode)
                .orElseThrow(() -> {
                    // 일지가 없을 경우 예외를 던집니다.
                    log.error("Journal not found for code: {}", journalCode);
                    return new NotFoundException(NOT_FOUND_JOURNAL_CODE);
                });

        // 해당 일지에 있는 모든 참가자의 이름을 리스트로 가져옴
        List<String> participantNamesList = journal.getParticipants().stream()
                .map(participant -> participant.getCustomer().getName())
                .collect(Collectors.toList());

        // 여러 참가자가 있는 경우 이름을 쉼표와 공백으로 구분하여 조인
        String participantNames = String.join(", ", participantNamesList);

        // 조회된 일지를 CustomerJournalResponse로 변환하여 반환합니다.
        log.info("Journal found: {}", journal);
        // CustomerJournalResponse.from 메서드에서 어떻게 변환되는지 로그합니다.
        log.debug("Converting Journal to CustomerJournalResponse: {}", CustomerJournalResponse.from(journal));

        // 해당 일지에 있는 모든 첨부파일의 URL을 리스트로 가져옴
        List<String> attachmentUrls = journal.getAttachments().stream()
                .map(Attachment::getUrl)
                .collect(Collectors.toList());

        // 생성된 participantNames를 CustomerJournalResponse 생성자에 추가하여 반환합니다.
        return new CustomerJournalResponse(
                journal.getProgram().getCategory().getCategoryName(),
                journal.getProgram().getRound(),
                journal.getObservation(),
                journal.getProgramTopic(),
                journal.getProgram().getStartTime(),
                journal.getProgram().getEndTime(),
                journal.getProgram().getDay(),
                journal.getEmployee().getEmployeeName(),
                journal.getProgram().getTeacher().getTeacherName(),
                journal.getSubProgress(),
                journal.getObserve(),
                journal.getRating(),
                journal.getNote(),
                participantNames,
                attachmentUrls
        );
    }

    // 4. 일지 등록 - (참관한 직원, 관리자)
    public Long save(List<MultipartFile> journalImages, JournalCreateRequest journalRequest) {
        try {
            // 이미지 파일 저장
            Set<String> existingFileNames = new HashSet<>();
            Journal journal = new Journal();

            // 각 이미지를 처리합니다.
            for (MultipartFile journalImg : journalImages) {
                String originalFileName = journalImg.getOriginalFilename();
                System.out.println("이미지 파일 저장: " + originalFileName);

                // 중복 체크
                if (!existingFileNames.add(originalFileName)) {
                    throw new DuplicateAttachmentException("Duplicate attachment found: " + originalFileName);
                }

                Attachment attachment = new Attachment();
                attachment.setUrl(originalFileName);
                attachment.setSeperation(1L); // 구분 설정, 예시로 1L로 설정

                // 각 첨부파일을 일지에 추가
                journal.addAttachment(attachment);

                // 이미지 파일 저장 로직 호출
                String savedFileName = saveImageFile(journalImg);
                attachment.setUrl(savedFileName);

//                // 일지가 존재하는지 확인하고, 없으면 새로운 일지를 생성합니다.
//                journal = journalRepository.findByJournalCode(journalRequest.getJournalCode())
//                        .orElseGet(Journal::new);
            }

            System.out.println("일지 존재하는지 조회 없으면 생성 : " + journal.getJournalCode());

            // 참가자 엔터티 생성 및 설정/////////////////////////////////////
            List<Participant> participants = Arrays.stream(journalRequest.getParticipantNames().split(","))
                    .map(participantName -> {
                        Customer customer = (Customer) customerRepository.findByName(participantName.trim())
                                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER_CODE));
                        return new Participant(customer);
                    })
                    .collect(Collectors.toList());

            journal.setParticipants(participants);

            System.out.println("참가자 설정 및 일지 저장 : " + participants.size() + "명, 일지 코드: " + journal.getJournalCode());

            // 새로운 일지를 생성하고, 기타 세부 정보를 설정한 후 저장합니다.
            journal.setJournalCode(journalRequest.getJournalCode());
            journal.setSubProgress(journalRequest.getSubProgress());
            journal.setObserve(journalRequest.getObserve());
            journal.setRating(journalRequest.getRating());
            journal.setNote(journalRequest.getNote());
            journal.setObservation(journalRequest.getObservation());
            journal.setProgramTopic(journalRequest.getProgramTopic());

            // 직원과 프로그램 엔터티 설정
            String employeeName = journalRequest.getEmployeeName();
            log.info("직원 이름으로 조회를 시도합니다: {}", employeeName);

            // 참가자들 설정
            Employee employee = (Employee) employeeRepository.findByEmployeeName(journalRequest.getEmployeeName())
                    .orElseThrow(() -> {
                        // 직원이 없을 경우 예외
                        log.error("직원을 찾을 수 없습니다. 직원 이름: {}", journalRequest.getJournalCode());
                        return new NotFoundException(NOT_FOUND_EMPLOYEE_NAME);
                    });
            log.info("직원을 찾았습니다: {}", employee.getEmployeeName());

            Program program = (Program) programRepository.findByCategoryCategoryNameAndRound(journalRequest.getCategoryName(), journalRequest.getRound())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

            Teacher teacher = program.getTeacher(); // 프로그램과 관련된 선생님 정보 가져오기

            journal.setEmployee(employee);
            journal.setProgram(program);

            System.out.println("직원 및 프로그램 설정 : " + employee.getEmployeeName() + ", " + program.getCode());

            // 프로그램 엔터티 설정
            program = (Program) programRepository.findByCategoryCategoryNameAndRound(journalRequest.getCategoryName(), journalRequest.getRound())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

            // 이 부분에서 setTeacherName이 아닌, Teacher 인스턴스를 바로 설정합니다.
            program.getTeacher().setTeacherName(teacher.getTeacherName());

            // 프로그램 설정
            journal.setProgram(program);

            // 일지를 저장합니다.
            journal = journalRepository.save(journal);
            System.out.println("일지 저장 : " + journal.getJournalCode());

            return journal.getJournalCode(); // 성공적으로 저장된 후 일지 코드를 반환합니다.
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("일지 저장 중 오류 발생");
        }
    }

    // 이미지 파일 저장 로직
    private String saveImageFile(MultipartFile imageFile) throws IOException {
        // 파일명 중복을 방지하기 위해 UUID를 이용하여 파일명 생성
        String replaceFileName = UUID.randomUUID().toString().replace("-", "");
        // 이미지 파일을 지정된 디렉토리에 저장
        FileUploadUtils.saveFile(IMAGE_DIR, replaceFileName, imageFile);
        // 저장된 파일명 반환
        return replaceFileName;
    }

    // 5. 일지 수정
    public void update(final Long journalCode, final MultipartFile journalImg, final JournalUpdateRequest journalRequest, boolean isCenterDirectorOrTeamLeader) {
        try {
            // 프로그램 조회
            Program program = (Program) programRepository.findByCategoryCategoryNameAndRound(journalRequest.getCategoryName(), journalRequest.getRound())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

            // 일지 조회
            Journal journal = journalRepository.findById(journalCode)
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_JOURNAL_CODE));

            // 이미지 수정 시 새로운 이미지 저장 후 기존 이미지 삭제 로직 필요함
            if (journalImg != null) {
                // 새로운 이미지 저장
                String replaceFileName = saveImageFile(journalImg);

                // 기존 이미지 삭제
                FileUploadUtils.deleteFile(IMAGE_DIR, journal.getAttachments().iterator().next().getUrl().replace(IMAGE_URL, ""));

                // 일지 첨부파일 URL 업데이트
                Attachment attachment = new Attachment();
                attachment.setUrl(replaceFileName);
                attachment.setSeperation(1L); // 구분 설정, 예시로 1L로 설정

                // 각 첨부파일을 일지에 추가
                journal.addAttachment(attachment);

                System.out.println("이미지 수정 완료: " + replaceFileName);
            }

            // 정보 업데이트
            journal.setSubProgress(journalRequest.getSubProgress());
            journal.setObserve(journalRequest.getObserve());
            journal.setRating(journalRequest.getRating());
            journal.setNote(journalRequest.getNote());
            journal.setObservation(journalRequest.getObservation());
            journal.setProgramTopic(journalRequest.getProgramTopic());
            System.out.println("일지 정보 업데이트 완료");

            // 참가자 업데이트
            List<Participant> participants = Arrays.stream(journalRequest.getParticipantNames().split(", "))
                    .map(participantName -> {
                        Customer customer = (Customer) customerRepository.findByName(participantName)
                                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER_CODE));
                        return new Participant(journal, customer);
                    })
                    .collect(Collectors.toList());
            journal.setParticipants(participants);
            System.out.println("참가자 업데이트 완료: " + participants.size() + "명");

            // 직원 업데이트
            String employeeName = journalRequest.getEmployeeName();
            Employee employee = (Employee) employeeRepository.findByEmployeeName(employeeName)
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_EMPLOYEE_NAME));
            System.out.println("직원 업데이트 완료: " + employee.getEmployeeName());

            // 현재 사용자가 직원 이름을 기반으로 일지를 업데이트할 권한이 있는지 확인
            if (!isCenterDirectorOrTeamLeader && !isCurrentUserJournalEmployee(employeeName)
                    && !SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equalsIgnoreCase("센터장") || role.getAuthority().equalsIgnoreCase("팀장"))) {
                throw new RuntimeException("권한 거부: 현재 사용자는 이 일지를 업데이트할 권한이 없습니다");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("유저 권한: " + authentication.getAuthorities());


            // 프로그램 업데이트
            journal.setProgram(program);
            System.out.println("프로그램 업데이트 완료: " + program.getCode());

            // 일지를 저장하여 업데이트 반영
            journalRepository.save(journal);
            System.out.println("일지 저장 완료");

        } catch (NotFoundException e) {
            log.error("프로그램 또는 일지를 찾을 수 없습니다.", e);
            throw e;
        } catch (Exception e) {
            log.error("일지 수정 중 오류 발생", e);
            throw new RuntimeException("일지 수정 중 오류 발생");
        }
    }

    // 현재 사용자가 일지의 직원인지 확인하는 도우미 메서드
    private boolean isCurrentUserJournalEmployee(String requestedEmployeeName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername().equals(requestedEmployeeName);
        }
        return false;
    }

    // 6. 일지 삭제(등록한 직원, 관리자)
    public void delete(Long journalCode) {
        // 일지 조회
        Journal journal = journalRepository.findById(journalCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_JOURNAL));
        try {
            // 모든 첨부파일 삭제
            for (Attachment attachment : journal.getAttachments()) {
                String url = attachment.getUrl().replace(IMAGE_URL, "");
                FileUploadUtils.deleteFile(IMAGE_DIR, url);
            }
            // 정보 삭제
            journalRepository.delete(journal);
            System.out.println("일지 삭제 완료");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("일지 삭제 중 오류 발생");
        }
    }

}