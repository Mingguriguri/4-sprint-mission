package com.sprint.mission.discodeit.entity;

/**
 * 레코드(엔티티)의 삭제/보관/복구 라이프사이클 상태를 상세히 표현하는 ENUM
 */
public enum RecordStatus {
    // 정상(아직 삭제 요청도, 삭제 처리도 되지 않은 상태)
    ACTIVE("정상"),
    // 소프트 삭제 처리되어 일반 조회에서 제외된 상태
    DELETED("소프트 삭제됨"),
    // 물리 삭제가 이루어진 상태 (DB 상에는 존재하지 않음)
    HARD_DELETED("물리 삭제됨");

    private final String recordStatus;

    RecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }
}
