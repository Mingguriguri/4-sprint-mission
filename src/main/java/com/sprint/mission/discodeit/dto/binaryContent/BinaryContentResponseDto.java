package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BinaryContentResponseDto {
    UUID id;
    byte[] bytes;
    BinaryContentType type;
    Instant createdAt;
}
