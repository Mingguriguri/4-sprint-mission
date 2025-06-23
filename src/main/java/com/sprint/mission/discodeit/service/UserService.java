package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDtos;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User create(UserCreateDto createUserRequestDto);
    UserResponseDto find(UUID userId);
    UserResponseDtos findAll();
    User update(UserUpdateDto updateUserRequestDto);
    void delete(UUID userId);
}
