package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JCFUserRepository implements UserRepository {
    private final List<User> userRepository = new ArrayList<>();

    @Override
    public User save(User user) {
        userRepository.removeIf(u -> u.getId().equals(user.getId()));
        userRepository.add(user);
        return user;
    }

    @Override
    public List<User> findAllByRecordStatusIsActive() {
        return userRepository.stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByMemberStatusIsActiveAndId(String id) {
        return userRepository.stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByMemberStatusIsInactiveAndId(String id) {
        return userRepository.stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getStatus() == UserStatus.INACTIVE)
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findByEmail(String email) {
        return userRepository.stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getEmail().equals(email))
                .toList();
    }

    @Override
    public List<User> findByUsername(String username) {
        return userRepository.stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getUsername().equals(username))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        findByMemberStatusIsActiveAndId(id).ifPresent(u -> {
                    u.inactivate();
                    u.softDelete();
                    u.touch();
                });
    }

    @Override
    public void restoreById(String id) {
        findById(id).ifPresent(u -> {
                    u.activate();
                    u.restore();
                    u.touch();
                });
    }

    @Override
    public void deleteById(String id) {
        userRepository.removeIf(u -> u.getId().equals(id));
    }
}
