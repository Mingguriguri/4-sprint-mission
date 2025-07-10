package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChannelResponseDto {
    @Schema(description = "채널 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "채널 타입", example = "PUBLIC")
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

    @Schema(description = "[PRIVATE 전용] 마지막으로 메시지를 보낸 시간", example = "2025-07-08T09:55:59.735Z")
    private Instant lastMessageSentAt;
}
