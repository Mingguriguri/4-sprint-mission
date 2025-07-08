package com.sprint.mission.discodeit.dto.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class AllChannelByUserIdResponseDto {
    private final List<ChannelResponseDto> publicChannels;
    private final List<ChannelResponseDto> privateChannels;
}
