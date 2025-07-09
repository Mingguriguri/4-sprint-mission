package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateDto {
    @Schema(description = "메시지 내용", example = "오늘의 공지 확인해주세요.")
    @NotBlank
    private final String content;

    @Schema(description = "채널 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private final UUID channelId;

    @Schema(description = "작성자 ID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    @NotNull
    private final UUID authorId;

    @Schema(description = "첨부파일 리스트")
    @Setter
    private List<BinaryContentCreateDto> attachments;
}
