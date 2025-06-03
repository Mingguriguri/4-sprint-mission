package com.sprint.mission.discodeit.entity;

/**
 * 유저의 상태를 나타내는 ENUM
 */
public enum UserStatus {
    ACTIVE("활동 회원"),
    INACTIVE("휴면 회원"),
    DELETED("삭제 회원");

    public final String status;
    UserStatus(String status) {
        this.status = status;
    }
}
