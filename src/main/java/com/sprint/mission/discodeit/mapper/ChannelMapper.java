package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ChannelMapper {
    /**
     * ChannelCreateDto → Channel 엔티티 변환
     */
    public Channel toEntity(ChannelCreateDto dto) {
        return new Channel(
                dto.getChannelType(),
                dto.getName(),
                dto.getDescription(),
                dto.getParticipantIds()
        );
    }

    /**
     * ChannelUpdateDto → 기존 Channel 엔티티 덮어쓰기
     */
    public void updateEntity(ChannelUpdateDto dto, Channel channel) throws IllegalArgumentException {
        if (channel.getType() != dto.getType()) {
            throw new IllegalArgumentException("채널 타입은 변경할 수 없습니다.");
        }
        if (dto.getType() == ChannelType.PRIVATE) {
            throw new IllegalArgumentException("PRIVATE 채널은 수정할 수 없습니다.");
        }
        if (channel.getType() == ChannelType.PUBLIC) {
            if (dto.getName() != null)        channel.updateName(dto.getName());
            if (dto.getDescription() != null) channel.updateDescription(dto.getDescription());
        }
    }

    /**
     * Channel → ChannelResponseDto 변환
     */
    public ChannelResponseDto toDto(Channel channel, List<UUID> participantIds) {
        return new ChannelResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                channel.getLastMessageSentAt()
        );
    }
}
