package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/*
* 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다.
* 사용자의 온라인 상태를 확인하기 위해 활용합니다.
* */
@Getter
public class UserStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt; // 접속시간

    private UUID userId;

    public UserStatus(UUID userId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.userId = userId;
    }

    public void touch() {
        this.updatedAt = Instant.now();
    }
    /*
    * 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드
    * */
    public boolean isOnline() {
        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
        return updatedAt != null &&
                updatedAt.isBefore(Instant.now().plusSeconds(300));
    }
}
