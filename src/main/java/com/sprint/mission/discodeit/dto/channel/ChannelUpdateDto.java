package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ChannelUpdateDto {
    /**
     *  Public Channel만 수정 가능
     */
    @Schema(description = "채널 ID", example = "8fba4d61-84c2-4d84-9808-ded529f5ecca")
    @NotNull
    private final UUID id;

    @Schema(description = "채널 타입", example = "PUBLIC")
    @NotNull
    private final ChannelType type;

    @Schema(description = "[PUBLIC 전용] 채널명", example = "📒학습-공지")
    @NotBlank
    private final String name;

    @Schema(description = "[PUBLIC 전용] 채널 설명", example = "📒학습-공지 채널의 시작이에요. 학습 관련 사항이 공지되는 채널이에요. 이 채널에 공유되는 소식은 모든 멤버가 꼭 확인해 주세요!")
    private final String description;
}
