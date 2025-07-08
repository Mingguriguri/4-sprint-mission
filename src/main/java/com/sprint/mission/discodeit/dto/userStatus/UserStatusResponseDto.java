package com.sprint.mission.discodeit.dto.userStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class UserStatusResponseDto {
    private final UUID id;
    private final UUID userId;
    private final Instant lastConnectedAt;
}
