package com.sprint.mission.discodeit.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.*;

// UserPatchDto로 PATCH 시 부분 수정만 되도록 설정했습니다
// 그러기 위해서 @NoArgsConstructor, @Setter 추가해서 imageFile 없을 땐 null로 바인딩되게 했습니다.
@Getter @Setter
@NoArgsConstructor
@Schema(name = "UserUpdateDto", description = "사용자 수정 요청 DTO")
public class UserUpdateDto {
    @Schema(description = "사용자명", example = "mingguriUpdate")
    private String newUsername;

    @Schema(description = "이메일", example = "mingguriguri123@gmail.com")
    @Email
    private String newEmail;

    @Schema(description = "비밀번호", example = "1q2w3e4r!")
    private String newPassword;
}
