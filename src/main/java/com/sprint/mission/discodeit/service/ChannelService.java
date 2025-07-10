package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.*;

import java.util.UUID;

public interface ChannelService {
    ChannelResponseDto createPublicChannel(ChannelCreateDto dto);
    ChannelResponseDto createPrivateChannel(ChannelCreateDto dto);

    AllChannelByUserIdResponseDto findAllByUserId(UUID userId);
    ChannelResponseDto find(UUID channelId, UUID userId);

    ChannelResponseDto update(UUID channelId, ChannelUpdateDto dto);

    void delete(UUID channelId);
}
