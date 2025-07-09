package com.sprint.mission.discodeit.dto.message;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
@ToString
public class MessageResponseDto {
    @Schema(description = "메시지 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private final UUID id;

    @Schema(description = "채널 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private final UUID channelId;

    @Schema(description = "작성자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private final UUID authorId;

    @Schema(description = "메시지 내용", example = "오늘의 공지 확인해주세요.")
    private final String content;

    @Schema(description = "첨부파일 ID 리스트", example = "[3fa85f64-5717-4562-b3fc-2c963f66afa6, 3fa85f64-5717-4562-b3fc-2c963f66afa6]")
    private final List<UUID> attachmentIds;

    @Schema(description = "생성일", example = "2025-07-08T09:55:59.735Z")
    private final Instant createdAt;

    @Schema(description = "수정일", example = "2025-07-08T09:55:59.735Z")
    private final Instant updatedAt;
}
