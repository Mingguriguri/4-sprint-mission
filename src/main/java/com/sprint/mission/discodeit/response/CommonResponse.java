package com.sprint.mission.discodeit.response;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 공통 응답 객체의 기본 클래스
 */
@Getter
public class CommonResponse<T> {
    private boolean success;       // 요청 처리 성공 여부
    private int code;               // 응답 상태 코드
    private String message;        // 클라이언트용 응답 메시지
    private T data;                // 실질적인 응답 데이터
    private LocalDateTime timestamp; // 응답 발생 시각

    public CommonResponse(boolean success, int code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, 200, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> CommonResponse<T> fail(int code, String message, T errorDetail) {
        return new CommonResponse<>(false, code, message, errorDetail);
    }
}
