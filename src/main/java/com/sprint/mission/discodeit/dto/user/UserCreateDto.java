package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

// TODO: 어노테이션 필요한 것만 남기기
@Getter
@Setter
@RequiredArgsConstructor
@Schema(name = "UserCreateDto", description = "사용자 생성 요청 DTO")
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

    @Schema(description="이미지 파일", type="string", format="binary")
    private MultipartFile imageFile;

    @Schema(description = "이미지 타입", example = "PROFILE")
    private BinaryContentType imageType;

}
