package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class BinaryContentResponseDto {
    private final UUID id;
    private final byte[] bytes;
    private final BinaryContentType type;
    private final Instant createdAt;
}
