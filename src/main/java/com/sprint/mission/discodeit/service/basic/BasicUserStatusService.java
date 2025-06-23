package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusCreateDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateDto;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    @Qualifier("JCFUserStatusRepository")
    private final UserStatusRepository userStatusRepository;

    @Qualifier("JCFUserRepository")
    private final UserRepository userRepository;

    @Override
    public UserStatusResponseDto create(UserStatusCreateDto userStatusCreateDto) {
        UUID userId = userStatusCreateDto.getUserId();
        // 관련된 User가 존재하지 않으면 예외
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + userId));

        // 같은 User와 관련된 객체가 이미 존재하면 예외
        if (userStatusRepository.existsById(userId)) {
            throw new IllegalArgumentException("User with id " + userId + " objects already exist");
        }

        UserStatus userStatus = new UserStatus(userId);
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto find(UUID id) {
        return userStatusRepository.findById(id)
                .map(UserStatusResponseDto::from)
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + id + " not found"));
    }

    @Override
    public List<UserStatusResponseDto> findAll() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public UserStatusResponseDto update(UserStatusUpdateDto userStatusUpdateDto) {
        UserStatus userStatus = userStatusRepository.findById(userStatusUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusUpdateDto.getId() + " not found"));
        // 사용자가 마지막으로 확인된 접속 시간 업데이트
        userStatus.touch();
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public UserStatusResponseDto updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus for userId " + userId + " not found"));
        // 사용자가 마지막으로 확인된 접속 시간 업데이트
        userStatus.touch();
        userStatusRepository.save(userStatus);
        return UserStatusResponseDto.from(userStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!userStatusRepository.existsById(id)) {
            throw new NoSuchElementException("UserStatus with id " + id + " not found");
        }
        userStatusRepository.deleteById(id);
    }
}
