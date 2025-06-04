package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * Base 엔티티는 다른 엔티티의 공통 속성을 담고 있는 엔티티이다.
 * <p>주요 속성:</p>
 * <ul>
 *   <li>id: 고유 ID (UUID로 초기화)</li>
 *   <li>createdAt: 생성시각 (현재 시각으로 초기화)</li>
 *   <li>updatedAt: 수정시각 (현재 시각으로 초기화)</li>
 *   <li>recordStatus: 레코드 삭제 상태 (ACTIVE, DELETED)</li>
 * </ul>
 * <p>
 * RecordStatus는 엔티티의 삭제 상태를 나타내며, 생성 시 ACTIVE로 초기화된다.
 * 소프트 삭제 시 DELETED로 변경된다.
 * </p>
 */

public class Base {
    private final String id;
    private final long createdAt;
    private long updatedAt;
    private RecordStatus recordStatus;

    public Base() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.recordStatus = RecordStatus.ACTIVE;
    }

    public String getId() {
        return id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
    }
}
