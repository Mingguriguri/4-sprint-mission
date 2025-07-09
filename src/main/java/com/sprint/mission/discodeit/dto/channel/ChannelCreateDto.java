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
    @Schema(description = "[PRIVATE 전용] 사용자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID userId;
    @Schema(description = "[PRIVATE 전용] 상대방 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID otherUserId;
}
