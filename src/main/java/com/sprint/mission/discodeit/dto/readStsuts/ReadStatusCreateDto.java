package com.sprint.mission.discodeit.dto.readStsuts;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor public class ReadStatusCreateDto {
    @NotNull
    private final UUID userId;
    @NotNull
    private final UUID channelId;
}
