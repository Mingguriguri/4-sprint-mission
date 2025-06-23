package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDtos;
import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    @Qualifier("JCFUserRepository")
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserResponseDto create(UserCreateDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();
        String password = requestDto.getPassword();
        UUID profileId = requestDto.getProfileId();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("This username" + username + "is already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email " + email + " is already in use");
        }
        if (profileId != null && !binaryContentRepository.existsById(profileId)) {
            throw new NoSuchElementException("Binary Content with id " +  profileId +" not found");
        }

        User user = new User(username, email, password, profileId);
        userRepository.save(user);

        // UserStatus도 함께 저장
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return UserResponseDto.from(user, userStatus.isOnline());
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        return UserResponseDto.from(user, userStatus.isOnline());
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserStatus> statusList = userStatusRepository.findAll();

        Map<UUID, UserStatus> statusMap = statusList.stream()
                .collect(Collectors.toMap(
                        UserStatus::getUserId,   // key: userId
                        Function.identity()      // value: UserStatus 객체 자체
                ));

        return userList.stream()
                .map(user -> {
                    UserStatus status = statusMap.get(user.getId());
                    boolean online = status != null && status.isOnline();
                    return UserResponseDto.from(user, online);
                }).toList();
    }

    @Override
    public UserResponseDto update(UserUpdateDto updateUserRequestDto) {
        User user = userRepository.findById(updateUserRequestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateUserRequestDto.getId() + " not found"));

        if (updateUserRequestDto.getProfileId() == null || !binaryContentRepository.existsById(updateUserRequestDto.getProfileId())) {
            throw new NoSuchElementException("Binary Content with id " +  updateUserRequestDto.getProfileId() +" not found");
        }

        user.updateUsername(updateUserRequestDto.getUsername());
        user.updateEmail(updateUserRequestDto.getEmail());
        user.updatePassword(updateUserRequestDto.getPassword());
        user.updateProfileId(updateUserRequestDto.getProfileId());
        user.touch();

        UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + user.getId() + " not found"));

        userRepository.save(user);
        return UserResponseDto.from(user, userStatus.isOnline());
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        userRepository.deleteById(userId);
        binaryContentRepository.deleteByProfileId(userId);
        userStatusRepository.deleteByUserId(userId);
    }
}
