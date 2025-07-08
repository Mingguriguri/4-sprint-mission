package com.sprint.mission.discodeit.dto.readStsuts;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class ReadStatusResponseDto {
    private final UUID id;
    private final Instant lastReadAt;
    private final UUID userId;
    private final UUID channelId;
}
