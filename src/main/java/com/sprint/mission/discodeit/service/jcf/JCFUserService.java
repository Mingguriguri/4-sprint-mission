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

    @Override
    public List<User> getAllUsers() {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .toList();
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> getUserByIdWithStatus(String id, RecordStatus recordStatus) {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(recordStatus))
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByUsername(String username) {
        return userList.stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(String username, String email, String password) {
        User user = new User(username, email, password, UserStatus.ACTIVE);
        userList.add(user);
        System.out.println("Successfully Create User, " + user);
        return user;
    }

    @Override
    public User updateUserInfo(String id, String username, String email, String password) {
        Optional<User> optionalUser = getUserById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            // 탈퇴한 사용자
            if (user.getStatus() == UserStatus.DELETED) {
                throw new IllegalArgumentException("This user is withdrew. Cannot update a deleted user: " + id);
            }
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setUpdatedAt(System.currentTimeMillis());

            return user;
        } else {
            throw new IllegalArgumentException("User with id " + id + "not found");
        }

    }

    // 사용자 비활성화
    @Override
    public void deactivateUser(User user) {
        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.ACTIVE);

        if (optionalUser.isPresent()) {
            user.setStatus(UserStatus.INACTIVE);
            user.setUpdatedAt(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found");
        }
    }

    // 비활성화된 사용자 다시 활성화
    @Override
    public void activateUser(User user) {
        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.ACTIVE);

        if (optionalUser.isPresent()) {
            if (user.getStatus() != UserStatus.INACTIVE) {
                // 기존에 비활성화되지 않았다면 예외
                throw new IllegalArgumentException("User is not inactive: " + user.getId());
            }
            user.setStatus(UserStatus.ACTIVE);
            user.setUpdatedAt(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found");
        }
    }


    @Override
    public void deleteUser(User user) {
        Optional<User> optionalUser = getUserById(user.getId());

        if (optionalUser.isPresent()) {
            // 메시지 Soft Delete
            List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
            copyOfMessages.forEach(msg -> {
                        msg.setRecordStatus(RecordStatus.DELETED);
                        msg.setUpdatedAt(System.currentTimeMillis());
                            });

            // 채널 Soft Delete
            List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
            for (Channel channel: copyOfChannels) {
                channel.setRecordStatus(RecordStatus.DELETED);
                channel.setUpdatedAt(System.currentTimeMillis());
            }

            // 유저 Soft Delete
            user.setStatus(UserStatus.DELETED);
            user.setRecordStatus(RecordStatus.DELETED);
            user.setUpdatedAt(System.currentTimeMillis());
        }
        else {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found");
        }
    }
    @Override
    public void restoreUser(User user) {
        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.DELETED);
        if (optionalUser.isPresent()) {
            // 메시지 복원
            List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
            for (Message msg: copyOfMessages) {
                msg.setRecordStatus(RecordStatus.ACTIVE);
                msg.setUpdatedAt(System.currentTimeMillis());
            }

            // 채널 복원
            List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
            for (Channel channel: copyOfChannels) {
                channel.setRecordStatus(RecordStatus.ACTIVE);
                channel.setUpdatedAt(System.currentTimeMillis());
            }

            // 유저 복원
            user.setStatus(UserStatus.ACTIVE);
            user.setRecordStatus(RecordStatus.ACTIVE);
            user.setUpdatedAt(System.currentTimeMillis());
        }
        else {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found");
        }
    }
    @Override
    public void hardDeleteUser(User user) {
        Optional<User> optionalUser = getUserByIdWithStatus(user.getId(), RecordStatus.DELETED);
        if (optionalUser.isPresent()) {
            // 메시지 삭제
            List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
            copyOfMessages.forEach(user::removeMessage);

            // 채널 삭제
            List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
            copyOfChannels.forEach(user::removeChannel);

            userList.remove(user);
        }
        else {
            throw new IllegalArgumentException("User with id " + user.getId() + " not found");
        }
    }
}
