package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    // CREATE or UPDATE
    User save(User user);

    // READ
    List<User> findAllByRecordStatusIsActive();
    Optional<User> findById(String id);
    Optional<User> findByMemberStatusIsActiveAndId(String id);
    Optional<User> findByMemberStatusIsInactiveAndId(String id);

    // 조회 조건
    List<User> findByEmail(String email);
    List<User> findByUsername(String username);

    // DELETE
    void softDeleteById(String id);
    void restoreById(String id);
    void deleteById(String id);
}

