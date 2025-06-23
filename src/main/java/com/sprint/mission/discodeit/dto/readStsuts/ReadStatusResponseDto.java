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
    private Instant updatedAt;
    private UUID userId;
    private UUID channelId;

    public static ReadStatusResponseDto from(ReadStatus r) {
        return new ReadStatusResponseDto(
                r.getId(),
                r.getLastReadAt(),
                r.getUserId(),
                r.getChannelId()
        );
    }
}
