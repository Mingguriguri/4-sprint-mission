package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PrivateChannelCreateDto {
    @NotBlank
    UUID userId;
}
