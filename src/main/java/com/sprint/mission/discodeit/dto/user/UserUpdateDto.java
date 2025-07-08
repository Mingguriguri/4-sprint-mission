package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserUpdateDto {
    @NotNull
    private final UUID id;

    @NotBlank
    private final String username;

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    private final String password;

    private final BinaryContentCreateDto binaryContent;
}
