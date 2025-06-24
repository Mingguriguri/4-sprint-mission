package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public UserResponseDto create(UserCreateDto requestDto) {
        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("This username " + username + " is already in use");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email " + email + " is already in use");
        }

        User createUser = userMapper.toEntity(requestDto);
        if(requestDto.getBinaryContent() != null) {
            BinaryContent binaryContent = binaryContentMapper.toEntity(requestDto.getBinaryContent());
            binaryContentRepository.save(binaryContent);
            createUser.updateProfileId(binaryContent.getId());
        }

        userRepository.save(createUser);

        // UserStatus도 함께 저장
        UserStatus userStatus = new UserStatus(createUser.getId());
        userStatusRepository.save(userStatus);

        return userMapper.toDto(createUser, userStatus.isOnline());
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user =  userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

        return userMapper.toDto(user, userStatus.isOnline());
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
                    return userMapper.toDto(user, online);
                }).toList();
    }

    @Override
    public UserResponseDto update(UserUpdateDto requestDto) {
        User updateUser = userRepository.findById(requestDto.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + requestDto.getId() + " not found"));

        if (requestDto.getBinaryContent() != null) {
            binaryContentRepository.deleteByProfileId(requestDto.getId()); // 기존 프로필 이미지 삭제
            BinaryContent binaryContent = binaryContentMapper.toEntity(requestDto.getBinaryContent());
            binaryContentRepository.save(binaryContent); // 새로운 프로필 이미지 저장
            updateUser.updateProfileId(binaryContent.getId());
        }

        userMapper.updateEntity(requestDto, updateUser);

        UserStatus userStatus = userStatusRepository.findByUserId(updateUser.getId())
                .orElseThrow(() -> new NoSuchElementException("User with id " + updateUser.getId() + " not found"));

        userRepository.save(updateUser);
        return userMapper.toDto(updateUser, userStatus.isOnline());
    }

    @Override
    public void delete(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User with id " + userId + " not found");
        }
        binaryContentRepository.deleteByProfileId(userId);
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }
}
