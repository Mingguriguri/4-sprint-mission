package com.sprint.mission.discodeit.entity;

/**
 * 레코드(엔티티)의 Soft Delete/Restore 상태를 나타내는 Enum.
 * <ul>
 *   <li>ACTIVE: 정상 상태 (삭제되지 않음)</li>
 *   <li>DELETED: 소프트 삭제된 상태 (일반 조회에서 제외됨)</li>
 * </ul>
 */

public enum RecordStatus {
    ACTIVE("정상"),
    DELETED("소프트 삭제됨");

    private final String recordStatus;

    RecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }
}
