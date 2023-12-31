package com.kimleepark.thesilver.board.program.service;

import com.kimleepark.thesilver.board.program.domain.Program;
import com.kimleepark.thesilver.board.program.domain.ProgramCategory;
import com.kimleepark.thesilver.board.program.domain.Teacher;
import com.kimleepark.thesilver.board.program.domain.repository.ProgramCategoryRepository;
import com.kimleepark.thesilver.board.program.domain.repository.ProgramRepository;
import com.kimleepark.thesilver.board.program.domain.repository.TeacherRepository;
import com.kimleepark.thesilver.board.program.dto.request.ProgramCreateRequest;
import com.kimleepark.thesilver.board.program.dto.request.ProgramUpdateRequest;
import com.kimleepark.thesilver.board.program.dto.response.CustomerProgramResponse;
import com.kimleepark.thesilver.board.program.dto.response.CustomerProgramsResponse;
import com.kimleepark.thesilver.board.program.dto.response.ResponseProgram;
import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.common.util.FileUploadUtils;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.PageImpl;
import com.kimleepark.thesilver.common.util.FileUploadUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_PROGRAM_CODE;
import static java.awt.SystemColor.window;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgramService {

    private final ProgramRepository programRepository;
    private final ProgramCategoryRepository programCategoryRepository;
    private final TeacherRepository teacherRepository;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    private Pageable getPageable(Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("code").descending());
    }

    // 1. 프로그램 목록 조회 - 페이징 (직원, 관리자)
    @Transactional(readOnly = true)
    public Page<CustomerProgramsResponse> getCustomerPrograms(Integer page) {

        Page<Program> programs = programRepository.findAll(getPageable(page));
        return programs.map(program -> CustomerProgramsResponse.from(program));
    }

    // 2. 프로그램 목록 조회 - 프로그램명 입력 검색 기준, 페이징 (직원, 관리자)
    @Transactional(readOnly = true)
    public Page<CustomerProgramsResponse> getCustomerProgramsByCategory(final Integer page, final String categoryName) {

        Page<Program> programs = programRepository.findByCategory_CategoryNameContaining(categoryName, getPageable(page));

        return programs.map(program -> CustomerProgramsResponse.from(program));
    }

    // 3. 프로그램 상세 조회 - code 로 프로그램 1개 조회(고객, 관리자)
    @Transactional(readOnly = true)
    public CustomerProgramResponse getCustomerProgram(final Long code) {

        // 프로그램 코드로 프로그램 조회
        Optional<Program> optionalProgram = Optional.ofNullable(programRepository.findByCode(code));

        // Optional이 비어있다면 NotFoundException을 던집니다.
        Program program = optionalProgram.orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

        // 프로그램을 CustomerProgramResponse로 변환하여 반환
        return CustomerProgramResponse.from(program);
    }

    // 4. 프로그램 등록 (관리자)
    public Long save(MultipartFile teacherImg, ProgramCreateRequest programRequest) {
        try {
            // 이미지 파일 저장
            String replaceFileName = saveImageFile(teacherImg);
            System.out.println("이미지 파일 저장 : " + replaceFileName);

            // 카테고리가 존재하는지 확인
            Optional<ProgramCategory> existingCategoryOpt =
                    programCategoryRepository.findByCategoryName(programRequest.getCategoryName());
            if (existingCategoryOpt.isPresent()) {
                // 이미 존재하는 경우 예외를 던지고 클라이언트에서 처리하도록 함
                throw new CategoryAlreadyExistsException("이미 존재하는 카테고리입니다.");
            }
            // 존재하지 않는 경우 새로운 카테고리를 생성
            ProgramCategory newCategory = new ProgramCategory();
            newCategory.setCategoryName(programRequest.getCategoryName());
            ProgramCategory category = programCategoryRepository.save(newCategory);
            System.out.println("새로운 카테고리 생성 : " + category.getCategoryName());

            // 새로운 강사를 생성합니다.
            Teacher teacher = new Teacher();
            teacher.setTeacherName(programRequest.getTeacherName());
            teacher.setBirthDate(programRequest.getBirthDate());
            teacher.setGender(programRequest.getGender());
            teacher.setPhone(programRequest.getPhone());
            teacher.setPostNo(programRequest.getPostNo());
            teacher.setAddress(programRequest.getAddress());
            teacher.setDetailAddress(programRequest.getDetailAddress());
            teacher.setProfilePicture(IMAGE_URL + replaceFileName); // 프로필 사진 URL 설정
            System.out.println("새로운 강사 생성 : " + teacher.getTeacherName());

            // 강사를 저장합니다.
            teacher = teacherRepository.save(teacher);
            System.out.println("Teacher saved with ID: " + teacher.getCode());

            // 새로운 프로그램을 생성하고, 카테고리, 강사 및 기타 세부 정보를 설정한 후 저장합니다.
            Program program = new Program();
            program.setCategory(category);
            program.setStartDate(programRequest.getStartDate());
            program.setEndDate(programRequest.getEndDate());
            program.setDay(programRequest.getDay());
            program.setRound(programRequest.getRound());
            program.setStartTime(programRequest.getStartTime());
            program.setEndTime(programRequest.getEndTime());
            program.setShortStory(programRequest.getShortStory());
            program.setTeacher(teacher);
            program.setEmployeeCode(programRequest.getEmployeeCode());
            System.out.println("새로운 프로그램 생성, 카테고리, 강사정보 설정 후 저장");

            // 프로그램을 저장합니다.
            program = programRepository.save(program);
            System.out.println("프로그램 저장 : " + program.getCode());

            return program.getCode(); // 성공적으로 저장된 후 프로그램 코드를 반환합니다.

        } catch (CategoryAlreadyExistsException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage()); // 클라이언트에서 이 메시지를 받아서 처리
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("프로그램 저장 중 오류 발생");
        }
    }

    // 이미지 파일 저장 로직
    private String saveImageFile(MultipartFile imageFile) throws IOException {
        // 파일명 중복을 방지하기 위해 UUID를 이용하여 파일명 생성
        String replaceFileName = UUID.randomUUID().toString().replace("-", "");
        // 이미지 파일의 확장자 추출
        String originalFilename = imageFile.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
        // 이미지 파일을 지정된 디렉토리에 저장 (확장자 포함)
        FileUploadUtils.saveFile(IMAGE_DIR, replaceFileName + fileExtension, imageFile);
        // 저장된 파일명 반환 (확장자 포함)
        return replaceFileName + fileExtension;
    }

    //5. 프로그램 수정(관리자)
    public void update(final Long programCode, final MultipartFile teacherImg, final ProgramUpdateRequest programRequest) {
        // 일단 프로그램 조회
        Program program = programRepository.findById(programCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));
        try {
            // 이미지 수정 시 새로운 이미지 저장 후 기존 이미지 삭제 로직 필요함
            if (teacherImg != null) {
                // 새로운 이미지 저장
                String replaceFileName = saveImageFile(teacherImg);
                // 기존 이미지 삭제
                FileUploadUtils.deleteFile(IMAGE_DIR, program.getTeacher().getProfilePicture().replace(IMAGE_URL, ""));
                // 강사 프로필 사진 URL 업데이트
                program.getTeacher().setProfilePicture(IMAGE_URL + replaceFileName);
            }
            // 프로그램 정보 업데이트
            program.setStartDate(programRequest.getStartDate());
            program.setEndDate(programRequest.getEndDate());
            program.setDay(programRequest.getDay());
            program.setRound(programRequest.getRound());
            program.setStartTime(programRequest.getStartTime());
            program.setEndTime(programRequest.getEndTime());
            program.setShortStory(programRequest.getShortStory());

            // 강사 정보 업데이트
            Teacher teacher = program.getTeacher();
            teacher.setTeacherName(programRequest.getTeacherName());
            teacher.setBirthDate(programRequest.getBirthDate());
            teacher.setGender(programRequest.getGender());
            teacher.setPhone(programRequest.getPhone());
            teacher.setPostNo(programRequest.getPostNo());
            teacher.setAddress(programRequest.getAddress());
            teacher.setDetailAddress(programRequest.getDetailAddress());

            // 프로그램 카테고리 정보 업데이트
            ProgramCategory category = program.getCategory();
            category.setCategoryName(programRequest.getCategoryName()); // 카테고리 이름 업데이트

            // 강사 정보 저장
            teacherRepository.save(teacher);
            //카테고리 이름 저장
            programCategoryRepository.save(category);
            // 프로그램 저장
            programRepository.save(program);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ProgramUpdateException("프로그램을 업데이트하는 데 실패했습니다. 나중에 다시 시도해주세요.", e);
        }
    }

    // 6. 프로그램 삭제(관리자)
    public void delete(Long programCode) {
        // 프로그램 조회
        Program program = programRepository.findById(programCode)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PROGRAM_CODE));

        try {
            // 이미지 파일 삭제
            String profilePicture = program.getTeacher().getProfilePicture().replace(IMAGE_URL, "");
            FileUploadUtils.deleteFile(IMAGE_DIR, profilePicture);

            // 프로그램, 카테고리, 강사 정보 삭제
            programRepository.deleteById(programCode);
            programCategoryRepository.deleteById(program.getCategory().getCategoryCode());
            teacherRepository.deleteById(program.getTeacher().getCode());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("프로그램 삭제 중 오류 발생");
        }
    }


    //-------------------------------------------------------------------------
    public  List<ResponseProgram> getMyProgram(Long employeeCode) {
        List<Program> programList = programRepository.findAll();
        List<ProgramCategory> programCategories = (List<ProgramCategory>) programCategoryRepository.findAll();

        log.info("afdaf : {}",programCategories);
        List<ResponseProgram> responsePrograms = programList.stream().map(program -> ResponseProgram.from(program,programCategories)).collect(Collectors.toList());

        return responsePrograms;
    }

    //--------------------------------------------------------------------

    public class ProgramUpdateException extends RuntimeException { //수정 예외처리
        public ProgramUpdateException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
