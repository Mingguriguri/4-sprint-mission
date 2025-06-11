package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final List<User> userList;

    public JCFUserService() {
        this.userList = new ArrayList<>();
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<User> getAllUsers() {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .toList();
    }

    @Override
    public Optional<User> getUserById(String userId) {
        validateNotNullId(userId);
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        validateNotNullUserEmailorName(email);
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByUsername(String username) {
        validateNotNullUserEmailorName(username);
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public User createUser(String username, String email, String password) {
        validateNotNullUserEmailorName(username);
        validateNotNullUserEmailorName(email);

        User user = new User(username, email, password, UserStatus.ACTIVE);
        userList.add(user);
        System.out.println("Successfully Create User, " + user);
        return user;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public User updateUserInfo(String userId, String username, String email, String password) {
        validateNotNullId(userId);

        Optional<User> optionalUser = getUserById(userId);
        validateActiveUser(optionalUser.get());

        User user = optionalUser.get();
        // 활동중인 유저만 수정 가능
        findUserOrThrow(userId, UserStatus.ACTIVE);

        user.changeUsername(username);
        user.updateUserEmail(email);
        user.changeUserPassword(password);
        user.touch();

        return user;
    }

    // 사용자 비활성화
    @Override
    public void deactivateUser(User user) {
        validateActiveUser(user);
        // 활성화된 유저만 체크
        findUserOrThrow(user.getId(), UserStatus.ACTIVE);
        user.inactivate();
        user.touch();
    }

    // 비활성화된 사용자 다시 활성화
    @Override
    public void activateUser(User user) {
        validateActiveUser(user);
        // 기존에 비활성화된 유저만 체크
        findUserOrThrow(user.getId(), UserStatus.INACTIVE);
        user.activate();
        user.touch();
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteUser(User user) {
        validateActiveUser(user);

        // 메시지 Soft Delete
        user.getMessages().forEach(msg -> {
            msg.softDelete();
            msg.touch();
        });

        // 채널 Soft Delete
        user.getChannels().forEach(msg -> {
                    msg.softDelete();
                    msg.touch();
                });

        // 유저 Soft Delete
        user.inactivate();
        user.softDelete();
        user.touch();

    }

    @Override
    public void restoreUser(User user) {
        validateDeletedUser(user);

        // 메시지 복원
        user.getMessages().forEach(msg -> {
            msg.restore();
            msg.touch();
        });

        // 채널 복원
        user.getChannels().forEach(msg -> {
            msg.restore();
            msg.touch();
        });

        // 유저 복원
        user.activate();
        user.restore();
        user.touch();
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
        userList.remove(user);
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */

    /**
     * 주어진 userId UserStatus에 해당하는 유저를 찾고 반환합니다.
     * 없으면 IllegalArgumentException을 던집니다.
     *
     * @param userId       조회할 유저의 ID
     * @param expectedUserStatus  기대하는 유저 활동 상태 (ACTIVE, INACTIVE, WITHDREW)
     * @return                조건에 맞는 User 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private User findUserOrThrow(String userId, UserStatus expectedUserStatus) {
        return userList.stream()
                .filter(user -> user.getId().equals(userId))
                .filter(user -> user.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(user -> user.getStatus() == expectedUserStatus)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("User with id " + userId +
                                " not found in user status: " + expectedUserStatus));    }

    /**
     * 유저 ID가 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param id 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
    }

    /**
     * 유저의 이메일이나 이름 데이터가 null인지 검사합니다.
     * 주로 외부에서 전달된 데이터 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param data 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullUserEmailorName(String data) {
        if (data == null) {
            throw new IllegalArgumentException("User Email or username cannot be null");
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
    private void validateInactiveUser(User user) {
        if (user == null || user.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("User must be ACTIVE and not null");
        }
        if (user.getStatus() != UserStatus.ACTIVE) {

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
        }
    }


}
