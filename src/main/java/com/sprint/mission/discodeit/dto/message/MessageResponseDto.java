package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class MessageResponseDto {
    private UUID id;
    private UUID channelId;
    private UUID authorId;
    private String content;
    private List<UUID> attachmentIds;
    private Instant createdAt;
    private Instant updatedAt;
}
