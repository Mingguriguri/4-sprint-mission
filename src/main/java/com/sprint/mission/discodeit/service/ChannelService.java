package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public interface ChannelService {
    PrivateChannelResponseDto createPrivateChannel(PrivateChannelCreateDto privateChannelRequestDto);
    PublicChannelResponseDto createPublicChannel(PublicChannelCreateDto publicChannelRequestDto);

    PrivateChannelResponseDto findPrivateChannel(UUID id);
    PublicChannelResponseDto findPublicChannel(UUID id);
    AllChannelByUserIdResponseDto findAllByUserId(UUID userId);

    PublicChannelResponseDto update(PublicChannelUpdateDto updatePublicChannelRequestDto);

    void delete(UUID channelId);
}
