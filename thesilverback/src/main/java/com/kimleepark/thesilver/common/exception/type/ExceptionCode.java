package com.kimleepark.thesilver.common.exception.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {

    FAIL_TO_UPLOAD_FILE(1001, "파일 저장에 실패하였습니다."),
    FAIL_TO_DELETE_FILE(1002, "파일 삭제에 실패하였습니다."),

    NOT_FOUND_CATEGORY_CODE(2000, "카테고리 코드에 해당하는 카테고리가 존재하지 않습니다."),

    // customers 에러 처리
    NOT_FOUND_CUSTOMER_CODE(3000, "고객 코드에 해당하는 고객이 존재하지 않습니다."),

    // 프로그램 에러 처리
    NOT_FOUND_PROGRAM_CODE(4005, "프로그램 코드에 해당하는 프로그램이 존재하지 않습니다."),
    NOT_FOUND_CORRECTION_CODE(4005, "프로그램 수정에 실패했습니다."),
    NOT_FOUND_JOURNAL_CODE(4005, "일지 수정에 실패했습니다."),

    // 검색 에러 처리
    NOT_FOUND_MULTIPLE_LOOKUPS(4005, "검색 결과가 없습니다."),
    NOT_FOUND_PROGRAM(1001, "프로그램을 찾지 못했습니다."),
    NOT_FOUND_JOURNAL(1001, "일지를 찾지 못했습니다."),
    NOT_FOUND_EMPLOYEE_NAME(4005, "해당 직원 이름을 찾지 못했습니다."),

    // 인증,인가 에러 처리
    FAIL_LOGIN(9000, "비밀번호가 일치하지 않습니다. (5회 오류 시 계정 잠금)"),
    UNAUTHORIZED(9001, "인증 되지 않은 요청입니다."),
    NOT_FOUND_EMPLOYEE_NUMBER(9002, "사번에 해당하는 직원을 찾을 수 없습니다."),
    ACCESS_DENIED(9003, "허가 되지 않은 요청입니다."),
    MANY_LOGIN_ATTEMPTS(9004, "비밀번호 5회 오류로 잠금된 계정입니다."),

    NOT_FOUND_EMPLOYEE_PWD(9004, "비밀번호가 맞지 않습니다."),
    // 임시 비밀번호 발급 에러 처리
    NOT_FOUND_EMPLOYEE_EMAIL(9101, "해당하는 이메일이 존재하지 않습니다."),
    NOT_FOUND_ACCOUNT_NUMBER(9102, "해당하는 사번이 존재하지 않습니다."),
    MISMATCH_NUMBER_EMAIL(9103, "사번과 이메일 주소가 일치하지 않습니다.."),
    // 라이센스 에러 처리
    START_IS_AFTER_END(9201, "종료 날짜가 시작 날짜보다 빠릅니다."),



    NOT_FOUND_MEMBER_CODE(4004, "멤버 코드에 해당하는 유저가 없습니다."),

    NOT_ENOUGH_STOCK(5000, "재고 부족으로 주문 불가합니다."),
    NOT_FOUND_VALID_ORDER(5001, "유효한 주문 건이 없습니다."),

    NOT_FOUND_REVIEW_CODE(6000, "리뷰 코드에 해당하는 리뷰가 존재하지 않습니다."),

    ALREADY_EXIST_REVIEW(6001, "이미  작성되어 작성할 수 없습니다."),

    ALREADY_EXIST_ATTEND(1,"이미 출근등록이 된 상태입니다.");

    private final int code;
    private final String message;
}
