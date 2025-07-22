package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * 사용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현하는 도메인 모델입니다.
 * 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
 */

@NoArgsConstructor
@Getter
@Entity
@Table(name = "read_statuses")
public class ReadStatus extends BaseUpdateEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    @Column(nullable = false)
    private Instant lastReadAt;

    public ReadStatus(User user, Channel channel, Instant lastReadAt) {
        this.lastReadAt = lastReadAt;
        this.user = user;
        this.channel = channel;
    }

    public void touch() {
        this.lastReadAt = Instant.now();
    }
}
