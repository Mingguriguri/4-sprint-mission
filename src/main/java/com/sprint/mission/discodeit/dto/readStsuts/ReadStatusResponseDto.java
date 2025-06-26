package com.sprint.mission.discodeit.dto.readStsuts;

import com.sprint.mission.discodeit.entity.ReadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReadStatusResponseDto {
    private UUID id;
    private Instant lastReadAt;
    private UUID userId;
    private UUID channelId;
}
