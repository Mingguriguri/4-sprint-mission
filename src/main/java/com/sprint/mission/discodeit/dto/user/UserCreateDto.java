package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@RequiredArgsConstructor
public class UserCreateDto {
    @Schema(description = "사용자명", example = "Mingguriguri")
    @NotBlank
    private final String username;

    @Schema(description = "이메일", example = "abc@gmail.com")
    @Email(message = "Invalid email")
    @NotBlank
    private final String email;

    @Schema(description = "비밀번호", example = "1q2w3e4r!")
    @NotBlank
    private final String password;

    @Schema(description = "바이너리 컨텐츠")
    private final BinaryContentCreateDto binaryContent;
}
