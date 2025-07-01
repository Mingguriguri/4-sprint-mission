package com.sprint.mission.discodeit.dto.userStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserStatusUpdateDto {
    @NotNull
    private UUID id;
}
