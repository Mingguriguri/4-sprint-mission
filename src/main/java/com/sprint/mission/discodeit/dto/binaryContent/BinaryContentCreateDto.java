package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@RequiredArgsConstructor
public class BinaryContentCreateDto {
    @Schema(description = "바이너리 컨텐츠 타입", example = "PROFILE")
    @NotNull
    private final BinaryContentType type;

    @Schema(description = "바이너리 파일")
    private final MultipartFile file;
}
