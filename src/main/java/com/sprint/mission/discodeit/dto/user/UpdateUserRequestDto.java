package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {
    @NotBlank
    private UUID id;

    private String username;
    private String email;
    private String password;

    @Nullable
    private UUID profileId;
}
