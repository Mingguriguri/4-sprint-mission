package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelUpdateDto {
    /**
     *  Public Channel만 수정 가능
     */
    @Schema(description = "채널 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private final UUID id;

    @Schema(description = "채널 타입", example = "PUBLIC")
    @NotNull
    private final ChannelType type;

    @Schema(description = "[PUBLIC 전용] 채널명", example = "공지 채널")
    @NotBlank
    private final String name;

    @Schema(description = "[PUBLIC 전용] 채널 설명", example = "공지를 하는 채널입니다.")
    private final String description;
}
