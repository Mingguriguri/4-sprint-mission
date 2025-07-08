package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
