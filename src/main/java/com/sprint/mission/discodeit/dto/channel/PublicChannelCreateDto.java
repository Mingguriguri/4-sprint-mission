package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicChannelCreateDto {
    @NotBlank
    String name;
    String description;
}
