package com.sprint.mission.discodeit.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateDto {
    String content;
    UUID channelId;
    UUID authorId;
    List<UUID> attachmentIds;
}
