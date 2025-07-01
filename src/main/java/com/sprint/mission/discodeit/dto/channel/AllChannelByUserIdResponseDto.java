package com.sprint.mission.discodeit.dto.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class AllChannelByUserIdResponseDto {
    private List<ChannelResponseDto> publicChannels;
    private List<ChannelResponseDto> privateChannels;
}
