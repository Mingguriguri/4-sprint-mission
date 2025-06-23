package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserResponseDtos;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserResponseDto create(UserCreateDto createUserRequestDto);
    UserResponseDto find(UUID userId);
    List<UserResponseDto> findAll();
    UserResponseDto update(UserUpdateDto updateUserRequestDto);
    void delete(UUID userId);
}
