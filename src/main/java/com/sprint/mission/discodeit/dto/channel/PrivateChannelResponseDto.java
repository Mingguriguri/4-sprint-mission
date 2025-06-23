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
public class PrivateChannelResponseDto {
    private UUID id;
    private UUID userId;
    private Instant lastMessageSentAt;

    public static PrivateChannelResponseDto from(Channel channel, UUID userId) {
        return new PrivateChannelResponseDto(
                channel.getId(),
                userId,
                channel.getLastMessageSentAt()
        );
    }
}
