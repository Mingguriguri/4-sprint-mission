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
public class MessageUpdateDto {
    @Schema(description = "메시지 ID", example = "cbd2c76f-0bf1-4927-847a-e13877fbbea1")
    @NotNull
    private final UUID id;

    @Schema(description = "메시지 내용", example = "3번째가 아니라 4번째 공지 확인해주세요. 확인 후 ✅ 이모지 눌러주세요!")
    @NotBlank
    private final String content;

    @Schema(description = "첨부파일 리스트")
    private final List<MultipartFile> attachments;

    @Schema(description = "첨부파일 타입", example = "MESSAGE")
    private final BinaryContentType attachmentType;
}
