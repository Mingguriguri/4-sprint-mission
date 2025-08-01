package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserCreateDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.FileAccessException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    public UserResponseDto create(UserCreateDto requestDto, MultipartFile profileImage) {
        validateCreate(requestDto);
        User createUser = userMapper.toEntity(requestDto);

        try {
            handleProfileImage(createUser, profileImage, BinaryContentType.PROFILE);
        } catch (IOException e) {
            // 트랜잭션시 롤백을 고려해서 RuntimeException을 상속받은 FileAccessException 형태로 예외 전환해서 던지도록 설정했습니다.
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        }
        userRepository.save(createUser);

        // UserStatus도 함께 저장
        UserStatus userStatus = new UserStatus(createUser.getId());
        userStatusRepository.save(userStatus);

        return userMapper.toDto(createUser, userStatus.isOnline());
    }

    @Override
    public UserResponseDto find(UUID userId) {
        User user = requireUser(userId);
        UserStatus userStatus = requireStatus(userId);
        return userMapper.toDto(user, userStatus.isOnline());
    }

    @Override
    public List<UserResponseDto> findAll() {
        List<User> userList = userRepository.findAll();
        List<UserStatus> statusList = userStatusRepository.findAll();

        Map<UUID, UserStatus> statusMap = statusList.stream()
                .collect(Collectors.toMap(
                        UserStatus::getUserId,
                        Function.identity()
                ));

        return userMapper.toDtoListWithStatus(userList, statusMap);
    }

    @Override
    public UserResponseDto update(UUID userId, UserUpdateDto dto, MultipartFile profileImage) {
        User existingUser = requireUser(userId);

        try {
            handleProfileImage(existingUser, profileImage, BinaryContentType.PROFILE);
        } catch (IOException e) {
            throw new FileAccessException(ErrorCode.FILE_IO_ERROR);
        }
        userMapper.updateEntity(dto, existingUser);
        userRepository.save(existingUser);
        UserStatus userStatus = requireStatus(existingUser.getId());

        return userMapper.toDto(existingUser, userStatus.isOnline());
    }

    @Override
    public void delete(UUID userId) {
        User user = requireUser(userId);
        if (user.getProfileId() != null) {
            binaryContentRepository.deleteById(user.getProfileId());
        }
        userStatusRepository.deleteByUserId(userId);
        userRepository.deleteById(userId);
    }


    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */
    /**
     * 사용자 생성 시 중복 여부를 검증합니다.
     *
     * @param dto 사용자 생성 요청 DTO
     * @throws IllegalArgumentException 사용자명 또는 이메일이 중복되는 경우
     */
    private void validateCreate(UserCreateDto dto) {
        validateExistUsername(dto.getUsername());
        validateExistEmail(dto.getEmail());
    }

    /**
     * 사용자명 중복 여부를 검증합니다.
     *
     * @param username 사용자명
     * @throws IllegalArgumentException null, 공백이거나 이미 존재하는 사용자명인 경우
     */
    private void validateExistUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("This username " + username + " is already in use");
        }
    }

    /**
     * 이메일 중복 여부를 검증합니다.
     *
     * @param email 이메일
     * @throws IllegalArgumentException null, 공백이거나 이미 존재하는 이메일인 경우
     */
    private void validateExistEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("This email " + email + " is already in use");
        }
    }

    /**
     * 프로필 이미지가 존재하면 저장하고 사용자 엔티티에 반영합니다.
     *
     * @param user 사용자 엔티티
     * @param file 이미지 파일
     * @param type 이미지 유형 (PROFILE)
     */
    private void handleProfileImage(User user,
                                    MultipartFile file,
                                    BinaryContentType type) throws IOException {
        if (file == null || file.isEmpty() || type == null) {
            return;
        }
        // 기존 이미지 삭제
        Optional.ofNullable(user.getProfileId())
                .ifPresent(binaryContentRepository::deleteById);

        // 새 이미지 저장
        BinaryContent bc = binaryContentMapper.toEntity(file, type);
        BinaryContent saved = binaryContentRepository.save(bc);

        user.updateProfileId(saved.getId());
    }

    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     * @throws NoSuchElementException 존재하지 않는 경우
     */
    private User requireUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
    }

    /**
     * 사용자 ID로 UserStatus 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return UserStatus 엔티티
     * @throws NoSuchElementException 존재하지 않는 경우
     */
    private UserStatus requireStatus(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new NoSuchElementException("UserStatus for id " + userId + " not found"));
    }

}