package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDtos;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User create(CreateUserRequestDto createUserRequestDto);
    UserResponseDto find(UUID userId);
    UserResponseDtos findAll();
    User update(UpdateUserRequestDto updateUserRequestDto);
    void delete(UUID userId);
}
