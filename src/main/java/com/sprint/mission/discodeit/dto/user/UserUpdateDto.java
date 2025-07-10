package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
@Schema(name = "UserUpdateDto", description = "사용자 수정 요청 DTO")
public class UserUpdateDto {
    @Schema(description = "사용자 ID", example = "ebfe591d-e39e-4a48-aa65-b489c4fc7d3a")
    @NotNull
    private final UUID id;

    @Schema(description = "사용자명", example = "mingguriUpdate")
    @NotBlank
    private final String username;

    @Schema(description = "이메일", example = "mingguriguri123@gmail.com")
    @Email
    @NotBlank
    private final String email;

    @Schema(description = "비밀번호", example = "1q2w3e4r!")
    @NotBlank
    private final String password;

    @Schema(description="이미지 파일", type="string", format="binary")
    private final MultipartFile imageFile;

    @Schema(description = "이미지 타입", example = "PROFILE")
    private final BinaryContentType imageType;
}
