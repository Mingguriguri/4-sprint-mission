package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserService implements UserService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public FileUserService(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByRecordStatusIsActive();
    }

    @Override
    public Optional<User> getUserById(String userId) {
        validateNotNullUserField(userId);
        return userRepository.findById(userId);
    }

    @Override
    public List<User> getUserByEmail(String email) {
        validateNotNullUserField(email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getUserByUsername(String username) {
        validateNotNullUserField(username);
        return userRepository.findByUsername(username);
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public User createUser(String username, String email, String password) {
        validateNotNullUserField(username);
        validateNotNullUserField(email);
        validateNotNullUserField(password);

        User newUser = new User(username, email, password);
        return userRepository.save(newUser);
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public User updateUserInfo(String userId, String username, String email, String password) {
        validateNotNullUserField(userId);
        validateNotNullUserField(username);
        validateNotNullUserField(email);
        validateNotNullUserField(password);

        User targetUser = userRepository.findByMemberStatusIsActiveAndId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found or not ACTIVE"));

        targetUser.changeUsername(username);
        targetUser.updateUserEmail(email);
        targetUser.changeUserPassword(password);
        targetUser.touch();
        return userRepository.save(targetUser);
    }

    // 사용자 비활성화
    @Override
    public void inactivateUser(User user) {
        User targetUser = userRepository.findByMemberStatusIsActiveAndId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found or not ACTIVE"));
        targetUser.inactivate();
        targetUser.touch();
        userRepository.save(targetUser);
    }

    // 비활성화된 사용자 다시 활성화
    @Override
    public void activateUser(User user) {
        User targetUser = userRepository.findByMemberStatusIsInactiveAndId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found or not ACTIVE"));

        targetUser.activate();
        targetUser.touch();
        userRepository.save(targetUser);
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteUser(User user) {
        validateActiveUser(user);

        // 메시지 Soft Delete
        List<Message> messages = messageRepository.findByUserId(user.getId());
        messages.forEach(msg -> {
            msg.softDelete();
            msg.touch();
        });
        for (Message m : messages) {
            messageRepository.softDeleteById(m.getId());
        }

        // 채널은 삭제될 때 관계만 끊어주기
        user.getChannels().forEach(msg -> {
            msg.softDelete();
            msg.touch();
        });

        // 유저 Soft Delete
        userRepository.softDeleteById(user.getId());
    }

    @Override
    public void restoreUser(User user) {
        // 메시지 복원
        user.getMessages().forEach(msg -> {
            msg.restore();
            msg.touch();
        });
        List<Message> messages = messageRepository.findByUserId(user.getId());
        for (Message m : messages) {
            messageRepository.restoreById(m.getId());
        }

        // 채널은 관계 다시 연결
        user.getChannels().forEach(msg -> {
            msg.restore();
            msg.touch();
        });

        // 유저 복원
        userRepository.restoreById(user.getId());
    }

    @Override
    public void hardDeleteUser(User user) {
        validateDeletedUser(user);

        // 메시지 관계 모두 제거
        List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
        copyOfMessages.forEach(user::removeMessage);

        // 채널 관계 모두 제거
        List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
        copyOfChannels.forEach(user::removeChannel);

        // 유저 제거
        userRepository.deleteById(user.getId());
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */

    /**
     * 유저의 ID나 이메일, 이름 데이터가 null인지 검사합니다.
     * 주로 외부에서 전달된 데이터 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param data 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullUserField(String data) {
        if (data == null || data.trim().isEmpty()) {
            throw new IllegalArgumentException("User Id or User Email or username cannot be null");
        }
    }

    /**
     * 유저가 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 유저를 수정하거나 삭제할 때 유효한 사용자여야 함을 보장합니다.
     *
     * @param user 검사할 User 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveUser(User user) {
        if (user == null || user.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("User must be ACTIVE and not null");
        }
    }

    /**
     * 유저가 null이거나 DELETED 상태가 아닌 경우 예외를 발생시킵니다.
     * 유저를 복원하거나 완전 삭제할 때 유효한 사용자여야 함을 보장합니다.
     *
     * @param user 검사할 User 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateDeletedUser(User user) {
        if (user == null || user.getRecordStatus() != RecordStatus.DELETED) {
            throw new IllegalArgumentException("User must be ACTIVE and not null");
//            System.out.println("HERE" + user.getRecordStatus());
        }
    }
}
