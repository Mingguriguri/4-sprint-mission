package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {
    UUID id;
    UUID channelId;
    UUID authorId;
    String content;
    List<UUID> attachmentIds;
    Instant createdAt;
    Instant updatedAt;
}
