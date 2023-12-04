package com.kimleepark.thesilver.board.journal.service;

import com.kimleepark.thesilver.attachment.Attachment;
import com.kimleepark.thesilver.board.journal.domain.Journal;
import com.kimleepark.thesilver.board.journal.domain.repository.JournalRepository;
import com.kimleepark.thesilver.board.journal.dto.request.JournalCreateRequest;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalResponse;
import com.kimleepark.thesilver.board.journal.dto.response.CustomerJournalsResponse;
import com.kimleepark.thesilver.board.participant.Participant;
import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.ProgramCategory;
import com.kimleepark.thesilver.board.program.domain.Teacher;
import com.kimleepark.thesilver.board.program.domain.repository.ProgramRepository;
import com.kimleepark.thesilver.board.program.dto.request.ProgramCreateRequest;
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
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
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
    private Journal journal;

    private Pageable getPageable(Integer page) {return PageRequest.of(page - 1, 10, Sort.by("journalCode").descending());}

    // 1. 일지 목록 조회
    @Transactional(readOnly = true)
    public Page<CustomerJournalsResponse> getCustomerProgramJournals(Integer page) {
        Pageable pageable = getPageable(page);

        Page<Journal> journals = journalRepository.findAllBy(pageable);

        List<CustomerJournalsResponse> responseList = journals.stream()
                .map(CustomerJournalsResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, journals.getTotalElements());
    }

    // 2. 일지 목록 조회 - 다중 검색 기준, 페이징 (직원, 프로그램 카테고리, 참관 일자)
    @Transactional(readOnly = true)
    public Page<CustomerJournalsResponse> getJournalsByMultipleCriteria(
            Integer page, String employeeCode, Long programCategoryCode, LocalDate observation) {

        Pageable pageable = getPageable(page);

        // 다중 검색 기준에 따라 동적으로 쿼리를 생성
        Specification<Journal> spec = Specification.where(null);

        if (StringUtils.hasText(employeeCode)) {
            spec = spec.and((root, query, builder) -> {
                // 직원 코드 검색 조건을 추가
                System.out.println("Adding employeeCode condition: " + employeeCode);
                return builder.equal(root.get("employee").get("employeeCode"), employeeCode);
            });
        }

        if (programCategoryCode != null) {
            spec = spec.and((root, query, builder) -> {
                // 프로그램 카테고리 코드 검색 조건을 추가
                System.out.println("Adding programCategoryCode condition: " + programCategoryCode);
                return builder.equal(root.get("program").get("category").get("categoryCode"), programCategoryCode);
            });
        }

        if (observation != null) {
            spec = spec.and((root, query, builder) -> {
                // 참관 일자 검색 조건을 추가
                System.out.println("Adding observation condition: " + observation);
                return builder.equal(root.get("observation"), observation);
            });
        }

        Page<Journal> journals = journalRepository.findAll(spec, pageable);

        if (journals.isEmpty()) {
            throw new CustomException(ExceptionCode.NOT_FOUND_MULTIPLE_LOOKUPS);
        }

        List<CustomerJournalsResponse> responseList = journals.stream()
                .map(CustomerJournalsResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, journals.getTotalElements());
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
                participantNames
        );
    }

    // 5. 일지 등록 - (참관한 직원, 관리자)
    public Long save(MultipartFile journalImg, JournalCreateRequest journalRequest) {
        try {
            // 이미지 파일 저장
            String replaceFileName = saveImageFile(journalImg);
            System.out.println("이미지 파일 저장 : " + replaceFileName);

            // 일지가 존재하는지 확인하고, 없으면 새로운 일지를 생성합니다.
            Journal journal = journalRepository.findByJournalCode(journalRequest.getJournalCode())
                    .orElseGet(() -> {
                        Journal newJournal = new Journal();
                        newJournal.setJournalCode(journalRequest.getJournalCode());
                        return newJournal;
                    });

            System.out.println("일지 존재하는지 조회 없으면 생성 : " + journal.getJournalCode());

            // 첨부파일 엔터티 생성 및 설정
            Attachment attachments = new Attachment();
            attachments.setUrl(replaceFileName);
            attachments.setSeperation(1L); // 구분 설정, 예시로 1L로 설정

            // 첨부파일과 일지 연관 설정
            attachments.setJournal(journal);
            journal.setAttachments(Collections.singletonList(attachments));

            System.out.println("첨부파일 설정 : " + attachments.getUrl());

            // 참가자 엔터티 생성 및 설정
            Journal finalJournal = journal;
            List<Participant> participants = Arrays.stream(journalRequest.getParticipantNames().split(", "))
                    .map(participantName -> {
                        Customer customer = (Customer) customerRepository.findByName(participantName)
                                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CUSTOMER_CODE));
                        return new Participant(finalJournal, customer);
                    })
                    .collect(Collectors.toList());
            journal.setParticipants(participants);

            // 새로운 일지를 생성하고, 기타 세부 정보를 설정한 후 저장합니다.
            journal.setSubProgress(journalRequest.getSubProgress());
            journal.setObserve(journalRequest.getObserve());
            journal.setRating(journalRequest.getRating());
            journal.setNote(journalRequest.getNote());
            journal.setObservation(journalRequest.getObservation());
            journal.setProgramTopic(journalRequest.getProgramTopic());

            System.out.println("참가자 설정 : " + participants.size() + "명");

            // 직원과 프로그램 엔터티 설정
            String employeeName = journalRequest.getEmployeeName();
            log.info("직원 이름으로 조회를 시도합니다: {}", employeeName);

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

            System.out.println("프로그램 설정 : " + program.getCode());

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


}
