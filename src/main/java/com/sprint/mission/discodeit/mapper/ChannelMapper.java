package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ChannelMapper {
    /**
     * ChannelCreateDto → Channel 엔티티 변환
     */
    public Channel toEntity(ChannelCreateDto dto) {
        return new Channel(
                dto.getType(),
                dto.getName(),
                dto.getDescription()
        );
    }

    /**
     * MessageUpdateDto → 기존 Channel 엔티티 덮어쓰기
     */
    public void updateEntity(ChannelUpdateDto dto, Channel channel) {
        if (channel.getType() != dto.getType()) {
            throw new IllegalArgumentException("채널 타입은 변경할 수 없습니다.");
        }
        if (channel.getType() == ChannelType.PUBLIC) {
            if (dto.getName() != null)        channel.updateName(dto.getName());
            if (dto.getDescription() != null) channel.updateDescription(dto.getDescription());
        }
        channel.touch();
    }

    /**
     * Public Channel → ChannelResponseDto 변환
     */
    public ChannelResponseDto toDtoPublic(Channel channel) {
        return new ChannelResponseDto(
                channel.getId(),
                ChannelType.PUBLIC,
                channel.getName(),
                channel.getDescription(),
                null,
                null,
                null
        );
    }

    /**
     * Private Channel → ChannelResponseDto 변환
     */
    public ChannelResponseDto toDtoPrivate(Channel channel, UUID userId, UUID otherUserId) {
        return new ChannelResponseDto(
                channel.getId(),
                ChannelType.PRIVATE,
                null,
                null,
                userId,
                otherUserId,
                channel.getLastMessageSentAt()
        );
    }
}
