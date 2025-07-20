package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelCreateDto {
    @Setter
    private ChannelType channelType;

    // PUBLIC 전용
    @Schema(description = "[PUBLIC 전용] 채널명", example = "공지 채널")
    private String name;
    @Schema(description = "[PUBLIC 전용] 채널 설명", example = "공지를 하는 채널입니다.")
    private String description;

    // PRIVATE 전용
    @Schema(description = "[PRIVATE 전용] 비공개 채널에 속한 사용자 ID 리스트",
            example = "[\"3fa85f64-5717-4562-b3fc-2c963f66afa6\",\"abcdef12-3456-7890-abcd-ef1234567890\"]")
    private List<UUID> participantIds;
}
