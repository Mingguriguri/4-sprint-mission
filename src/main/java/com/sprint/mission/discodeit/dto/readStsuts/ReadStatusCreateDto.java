package com.sprint.mission.discodeit.dto.readStsuts;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor public class ReadStatusCreateDto {
    @Schema(description = "사용자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private final UUID userId;
    @Schema(description = "채널 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private final UUID channelId;
}
