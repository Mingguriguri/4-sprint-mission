package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserResponseDtos;
import com.sprint.mission.discodeit.dto.user.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequestDto;
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
    public User create(CreateUserRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 username 입니다.");
        }
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 email 입니다.");
        }

        User user = new User(requestDto);

        // UserStatus도 함께 저장
        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return userRepository.save(user);
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getProfileId(),
                userStatus.isOnline(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Override
    public UserResponseDtos findAll() {
        List<User> userList = userRepository.findAll();
        List<UserStatus> statusList = userStatusRepository.findAll();

        Map<UUID, UserStatus> statusMap = statusList.stream()
                .collect(Collectors.toMap(
                        UserStatus::getUserId,   // key: userId
                        Function.identity()      // value: UserStatus 객체 자체
                ));

        List<UserResponseDto> dtoList = userList.stream()
                .map(user -> {
                    UserStatus status = statusMap.get(user.getId());
                    boolean online = status != null && status.isOnline();
                    return new UserResponseDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getProfileId(),
                        online,
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                    );
                }).toList();
        return new UserResponseDtos(dtoList);
    }

    @Override
    public User update(UpdateUserRequestDto updateUserRequestDto) {
        User user = userRepository.findById(updateUserRequestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateUserRequestDto.getId() + " not found"));

        user.updateUsername(updateUserRequestDto.getUsername());
        user.updateEmail(updateUserRequestDto.getEmail());
        user.updatePassword(updateUserRequestDto.getPassword());
        user.updateProfileId(updateUserRequestDto.getProfileId());
        user.touch();

        return userRepository.save(user);
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
