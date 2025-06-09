package com.sprint.mission.discodeit.entity;

/**
 * 유저의 활동 상태를 나타내는 Enum.
 * <ul>
 *   <li>ACTIVE: 활동 회원</li>
 *   <li>INACTIVE: 휴면 회원</li>
 *   <li>WITHDREW: 탈퇴 회원</li>
 * </ul>
 */
public enum UserStatus {
    ACTIVE("활동 회원"),
    INACTIVE("휴면 회원"),
    WITHDREW("탈퇴 회원");

    public final String status;
    UserStatus(String status) {
        this.status = status;
    }
}
