package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
        return userList;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userList.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByUsername(String username) {
        return userList.stream()
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
    public User updateUser(String id, String username, String email, String password, UserStatus status) {
        Optional<User> optionalUser = getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setStatus(status);
            user.setUpdatedAt(System.currentTimeMillis());

            return user;
        } else {
            throw new IllegalArgumentException("User with id " + id + "not found");
        }

    }

    @Override
    public void deleteUser(User user) {
        Optional<User> optionalUser = getUserById(user.getId());
        if (optionalUser.isPresent()) {
//            user.setStatus(UserStatus.DELETED);
//            user.setDeleted(true);

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
