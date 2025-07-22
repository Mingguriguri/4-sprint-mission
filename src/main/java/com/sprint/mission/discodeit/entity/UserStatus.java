package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
* 사용자 별 마지막으로 확인된 접속 시간을 표현하는 도메인 모델입니다.<br>
* 사용자의 온라인 상태를 확인하기 위해 활용합니다.
* */

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_statuses")
public class UserStatus extends BaseUpdateEntity  {

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private User user;

    @Column(name="last_active_at", nullable=false)
    private Instant lastConnectedAt;

    public UserStatus(User user) {
        this.user = user;
        this.lastConnectedAt = Instant.now();
    }

    public void updateLastConnectedAt() {
        this.lastConnectedAt = Instant.now();
    }

    /*
    * 마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드
    * */
    public boolean isOnline() {
        // 마지막 접속 시간이 현재 시간으로부터 5분 이내이면 현재 접속 중인 유저로 간주
        return lastConnectedAt != null &&
                lastConnectedAt.isAfter(Instant.now().minusSeconds(300));
    }
}
