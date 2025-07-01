package com.sprint.mission.discodeit.dto.readStsuts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class ReadStatusResponseDto {
    private UUID id;
    private Instant lastReadAt;
    private UUID userId;
    private UUID channelId;
}
