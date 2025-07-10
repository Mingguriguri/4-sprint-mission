package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelCreateDto {
    @Schema(description = "채널 타입", example = "PUBLIC")
    @NotNull
    private ChannelType type;

    // PUBLIC 전용
    @Schema(description = "[PUBLIC 전용] 채널명", example = "공지 채널")
    private String name;
    @Schema(description = "[PUBLIC 전용] 채널 설명", example = "공지를 하는 채널입니다.")
    private String description;

    // PRIVATE 전용
    @Schema(description = "[PRIVATE 전용] 사용자 ID", example = "55e3a449-2c32-4432-8d0d-28620130a8af")
    private UUID userId;
    @Schema(description = "[PRIVATE 전용] 상대방 ID", example = "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a")
    private UUID otherUserId;
}
