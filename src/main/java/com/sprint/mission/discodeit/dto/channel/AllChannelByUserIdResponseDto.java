package com.sprint.mission.discodeit.dto.channel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ToString
public class AllChannelByUserIdResponseDto {
    @Schema(description = "PUBLIC 타입의 채널 ResponseDTO 리스트")
    private final List<ChannelResponseDto> publicChannels;

    @Schema(description = "PRIVATE 타입의 채널 ResponseDTO 리스트")
    private final List<ChannelResponseDto> privateChannels;
}
