package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JCFUserService implements UserService {
    private final List<User> userList;
    private final MessageService messageService;

    public JCFUserService(MessageService messageService) {
        this.userList = new ArrayList<>();
        this.messageService = messageService;
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
        User user = new User(username, email, password);
        userList.add(user);
        return user;
    }

    @Override
    public User updateUser(String id, String username, String email, String password) {
        Optional<User> optionalUser = getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);
            user.setUpdatedAt(System.currentTimeMillis());

            return user;
        } else {
            throw new IllegalArgumentException("User with id " + id + "not found");
        }

    }

    @Override
    public void deleteUser(String id) {
        boolean removed = userList.removeIf(user -> user.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("User with id " + id + " not found");
        }
        messageService.deleteMessagesByUserId(id);
    }
}
