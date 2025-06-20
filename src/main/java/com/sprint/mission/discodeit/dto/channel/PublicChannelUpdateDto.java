package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PublicChannelUpdateDto {
    UUID id;
    String name;
    String description;
}
