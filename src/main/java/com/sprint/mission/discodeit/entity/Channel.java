package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelResponseDto;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastMessageSentAt; // 가장 최근 메시지의 시간

    private ChannelType type;
    private String name;
    private String description;

    public Channel(ChannelType type, String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Channel(CreatePrivateChannelRequestDto privateChannelRequestDto) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = ChannelType.PRIVATE;
        this.name = null;
        this.description = null;
    }

    public Channel(CreatePublicChannelRequestDto publicChannelRequestDto) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.type = ChannelType.PUBLIC;
        this.name = publicChannelRequestDto.getName();
        this.description = publicChannelRequestDto.getDescription();
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateLastMessageSentAt() {
        this.lastMessageSentAt = Instant.now();
    }

    public void touch() {
        this.updatedAt = Instant.now();
    }

}
