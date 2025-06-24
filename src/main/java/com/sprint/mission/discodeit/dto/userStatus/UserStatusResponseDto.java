package com.sprint.mission.discodeit.dto.userStatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusResponseDto {
    UUID id;
    UUID userId;
    Instant lastConnectedAt;
}
