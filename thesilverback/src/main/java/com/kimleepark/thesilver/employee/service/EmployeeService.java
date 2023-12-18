package com.kimleepark.thesilver.employee.service;

import com.kimleepark.thesilver.common.exception.NotFoundException;
import com.kimleepark.thesilver.common.util.FileUploadUtils;
import com.kimleepark.thesilver.employee.Employee;

import com.kimleepark.thesilver.employee.Rank;
import com.kimleepark.thesilver.employee.Team;
import com.kimleepark.thesilver.employee.dto.request.EmployeeUpdateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesCreateRequest;
import com.kimleepark.thesilver.employee.dto.request.EmployeesUpdateRequest;
import com.kimleepark.thesilver.employee.dto.response.CustomerEmployeesResponse;
import com.kimleepark.thesilver.employee.repository.EmployeeRepository;
import com.kimleepark.thesilver.employee.repository.RankRepository;
import com.kimleepark.thesilver.employee.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static com.kimleepark.thesilver.common.exception.type.ExceptionCode.NOT_FOUND_CATEGORY_CODE;
import static com.kimleepark.thesilver.employee.type.LeaveType.NO;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RankRepository rankRepository;
    private final TeamRepository teamRepository;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("employeeCode").descending());
    }

    @Transactional(readOnly = true)
    public Page<CustomerEmployeesResponse> getCustomerEmployeesManager(final Integer page){
        Page<Employee> employees = employeeRepository.findByLeaveType(getPageable(page), NO);

        return employees.map(employee -> CustomerEmployeesResponse.from(employee));
    }

    public CustomerEmployeesResponse getCustomerEmployeeManager(Long employeeCodeCode) {
        Employee employee = employeeRepository.findByEmployeeCodeAndLeaveType(employeeCodeCode, NO);
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CODE));

        return CustomerEmployeesResponse.from(employee);
    }

    public CustomerEmployeesResponse getCustomerEmployee(Long employeeCode) {
        Employee employee = employeeRepository.findByEmployeeCode(employeeCode).orElseThrow();
        return CustomerEmployeesResponse.from(employee);
    }

    private String getRandomName() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public void updates(final Long employeeCode, final MultipartFile multipartFile, final EmployeesUpdateRequest employeesUpdateRequest) {
        Employee employee = employeeRepository.findByEmployeeCodeAndLeaveType(employeeCode, NO);
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_PRODUCT_CODE));
        /* 이미지 수정 시 새로운 이미지 저장 후 기존 이미지 삭제 로직 필요 */
        Rank rank = rankRepository.findById(employeesUpdateRequest.getRankCode()).orElseThrow(()-> new IllegalArgumentException());
        Team team = teamRepository.findById(employeesUpdateRequest.getTeamCode()).orElseThrow(()-> new IllegalArgumentException());

        /* entity 정보 변경 */
        employee.updates(
                employeesUpdateRequest.getEmployeePicture(),
                rank,
                employeesUpdateRequest.getEmployeeName(),
                employeesUpdateRequest.getEmployeeEmail(),
                employeesUpdateRequest.getGender(),
                employeesUpdateRequest.getDisability(),
                employeesUpdateRequest.getMarriage(),
                employeesUpdateRequest.getPatriots(),
                employeesUpdateRequest.getEmploymentType(),
                employeesUpdateRequest.getWorkingStatus(),
                employeesUpdateRequest.getLeaveType(),
                employeesUpdateRequest.getRegistrationNumber(),
                employeesUpdateRequest.getEmployeePhone(),
                employeesUpdateRequest.getEmployeeAddress(),
                employeesUpdateRequest.getJoinDate(),
                employeesUpdateRequest.getLeaveDate(),
                employeesUpdateRequest.getLeaveReason(),
                team
        );
        if(multipartFile != null) {
//            /* 새로 입력 된 이미지 저장 */
//            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), multipartFile);
//            /* 기존 이미지 삭제 */
//            FileUploadUtils.deleteFile(IMAGE_DIR, employee.getEmployeePicture().replace(IMAGE_URL, ""));
//            /* entity 정보 변경 */
//            employee.updateEmployeePicture(IMAGE_URL + replaceFileName);
            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), multipartFile);
            employee.imgupdates(IMAGE_URL + replaceFileName);
        }

    }

    public void update(Long employeeCode, EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = employeeRepository.findByEmployeeCodeAndLeaveType(employeeCode, NO);

        employee.update(
                employeeUpdateRequest.getEmployeeEmail(),
                employeeUpdateRequest.getEmployeePhone(),
                employeeUpdateRequest.getEmployeeAddress()
        );
    }

    public void delete(Long employeeCode) {

        employeeRepository.deleteById(employeeCode);
    }

    public Long save(final MultipartFile multipartFile, final EmployeesCreateRequest employeesCreateRequest) {
        Rank rank = rankRepository.findById(employeesCreateRequest.getRankCode()).orElseThrow(() -> new IllegalArgumentException());
        Team team = teamRepository.findById(employeesCreateRequest.getTeamCode()).orElseThrow(() -> new IllegalArgumentException());

            /* 전달 된 파일을 서버의 지정 경로에 저장 */
//        Optional<Team> team = teamRepository.findById(employeesCreateRequest.getTeamCode())
//                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_CODE));

                final Employee newEmployee = Employee.of(
                        rank,
                        team,
                        employeesCreateRequest.getEmployeeName(),
                        employeesCreateRequest.getEmployeeEmail(),
                        employeesCreateRequest.getGender(),
                        employeesCreateRequest.getDisability(),
                        employeesCreateRequest.getMarriage(),
                        employeesCreateRequest.getPatriots(),
                        employeesCreateRequest.getEmploymentType(),
                        employeesCreateRequest.getWorkingStatus(),
                        employeesCreateRequest.getLeaveType(),
                        employeesCreateRequest.getRegistrationNumber(),
                        employeesCreateRequest.getEmployeePhone(),
                        employeesCreateRequest.getEmployeeAddress(),
                        employeesCreateRequest.getJoinDate()
                );

        if(multipartFile != null) {
            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), multipartFile);
                newEmployee.imgupdate(IMAGE_URL + replaceFileName);}

                final Employee employee = employeeRepository.save(newEmployee);

            return employee.getEmployeeCode();

        }

}
