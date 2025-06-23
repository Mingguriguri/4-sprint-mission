package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PublicChannelResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Instant lastMessageSentAt;

    public static PublicChannelResponseDto from(Channel channel) {
        return new PublicChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getLastMessageSentAt()
        );
    }
}
