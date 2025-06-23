package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
    private UUID profileId;
    private boolean isOnline;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserResponseDto from(User u, boolean online) {
        return new UserResponseDto(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getProfileId(),
                online,
                u.getCreatedAt(),
                u.getUpdatedAt()
        );
    }
}
