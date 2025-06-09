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
    public Optional<User> getUserById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByIdWithStatus(String id, RecordStatus recordStatus) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (recordStatus == null) {
            throw new IllegalArgumentException("RecordStatus cannot be null");
        }
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(recordStatus))
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByUsername(String username) {
        if (username == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
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
        User user = new User(username, email, password, UserStatus.ACTIVE);
        userList.add(user);
        System.out.println("Successfully Create User, " + user);
        return user;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public User updateUserInfo(String id, String username, String email, String password) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        Optional<User> optionalUser = getUserById(id);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + id + " not found or not ACTIVE");
        }

        User user = optionalUser.get();
        // 탈퇴한 사용자
        if (user.getStatus() == UserStatus.WITHDREW) {
            throw new IllegalArgumentException("This user is withdrew. Cannot update a deleted user: " + id);
        }
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setUpdatedAt(System.currentTimeMillis());

        return user;
    }

    // 사용자 비활성화
    @Override
    public void deactivateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }

        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.ACTIVE);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found or not ACTIVE");
        }

        if (user.getStatus() == UserStatus.INACTIVE) {
            // 이미 비활성화되어있다면 예외
            throw new IllegalArgumentException("User is already inactive: " + user.getId());
        }
        user.setStatus(UserStatus.INACTIVE);
        user.setUpdatedAt(System.currentTimeMillis());
    }

    // 비활성화된 사용자 다시 활성화
    @Override
    public void activateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.ACTIVE);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found or not INACTIVE");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            // 기존에 비활성화되지 않았다면 예외
            throw new IllegalArgumentException("User is already active: " + user.getId());
        }
        user.setStatus(UserStatus.ACTIVE);
        user.setUpdatedAt(System.currentTimeMillis());
    }

    /* =========================================================
     * DELETE
     * ========================================================= */

    @Override
    public void deleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        // recordStatus가 ACTIVE인지 확인
        if (!user.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("Cannot delete user whose recordStatus is not ACTIVE: " + user.getId());
        }

        // 메시지 Soft Delete
        user.getMessages().forEach(msg -> {
            msg.setRecordStatus(RecordStatus.DELETED);
            msg.setUpdatedAt(System.currentTimeMillis());
        });

        // 채널 Soft Delete
        user.getChannels().forEach(msg -> {
                    msg.setRecordStatus(RecordStatus.DELETED);
                    msg.setUpdatedAt(System.currentTimeMillis());
                });

        // 유저 Soft Delete
        user.setStatus(UserStatus.INACTIVE);
        user.setRecordStatus(RecordStatus.DELETED);
        user.setUpdatedAt(System.currentTimeMillis());

    }

    @Override
    public void restoreUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        if (!user.getRecordStatus().equals(RecordStatus.DELETED)) {
            throw new IllegalArgumentException("Cannot restore user whose recordStatus is not DELETED: " + user.getId());
        }

        // 메시지 복원
        user.getMessages().forEach(msg -> {
            msg.setRecordStatus(RecordStatus.ACTIVE);
            msg.setUpdatedAt(System.currentTimeMillis());
        });

        // 채널 복원
        user.getChannels().forEach(msg -> {
            msg.setRecordStatus(RecordStatus.ACTIVE);
            msg.setUpdatedAt(System.currentTimeMillis());
        });

        // 유저 복원
        user.setStatus(UserStatus.ACTIVE);
        user.setRecordStatus(RecordStatus.ACTIVE);
        user.setUpdatedAt(System.currentTimeMillis());
    }

    @Override
    public void hardDeleteUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User object cannot be null");
        }
        if (!user.getRecordStatus().equals(RecordStatus.DELETED)) {
            throw new IllegalArgumentException("Cannot hard delete user whose recordStatus is not DELETED: " + user.getId());
        }

        // 메시지 관계 모두 제거
        List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
        copyOfMessages.forEach(user::removeMessage);

        // 채널 관계 모두 제거
        List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
        copyOfChannels.forEach(user::removeChannel);

        // 유저 제거
        userList.remove(user);
    }
}
