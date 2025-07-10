package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MessageCreateDto {
    @Schema(description = "메시지 내용", example = "3번째 공지! 필독! 읽고 나서, ✅ 이모지 눌러주세요! ")
    @NotBlank
    private final String content;

    @Schema(description = "채널 ID", example = "fb306f45-af59-46b1-adcb-449f9d3dfb04")
    @NotNull
    private final UUID channelId;

    @Schema(description = "작성자 ID", example = "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a")
    @NotNull
    private final UUID authorId;

    @Schema(description = "첨부파일 리스트")
    private final List<MultipartFile> attachments;

    @Schema(description = "첨부파일 타입", example = "MESSAGE")
    private final BinaryContentType attachmentType;
}
