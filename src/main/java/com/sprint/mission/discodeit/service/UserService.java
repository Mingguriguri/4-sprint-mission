package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();

    Optional<User> getUserById(String id);

    List<User> getUserByEmail(String email);

    List<User> getUserByUsername(String username);

    User createUser(String username, String email, String password);

    User updateUser(String id, String username, String email, String password, UserStatus status);

    void deleteUser(User user);
}
