package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 */
@Getter
public class ReadStatus {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt; // == lastReadAt

    private UUID userId;
    private UUID channelId;

    public ReadStatus(UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.userId = userId;
        this.channelId = channelId;
    }

    public void updateUserId(UUID userId) {
        this.userId = userId;
    }

    public void updateChannelId(UUID channelId) {
        this.channelId = channelId;
    }

    public void touch() {
        this.updatedAt = Instant.now();
    }
}
