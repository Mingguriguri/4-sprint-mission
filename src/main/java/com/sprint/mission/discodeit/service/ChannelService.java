package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public interface ChannelService {
    Channel create(ChannelType type, String name, String description);
    Channel createPrivateChannel(CreatePrivateChannelRequestDto privateChannelRequestDto);
    Channel createPublicChannel(CreatePublicChannelRequestDto publicChannelRequestDto);

    PrivateChannelResponseDto findPrivateChannel(UUID channelId);
    PublicChannelResponseDto findPublicChannel(UUID channelId);
    AllChannelByUserIdResponseDto findAllByUserId(UUID userId);

    Channel update(UpdatePublicChannelRequestDto updatePublicChannelRequestDto);

    void delete(UUID channelId);
}
