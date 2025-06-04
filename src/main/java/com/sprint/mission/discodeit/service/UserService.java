package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    Optional<User> getUserByIdWithStatus(String id, RecordStatus recordStatus);
    List<User> getUserByEmail(String email);
    List<User> getUserByUsername(String username);

    User createUser(String username, String email, String password);

    User updateUserInfo(String id, String username, String email, String password);

    // 비즈니스 활동 상태 변경
    void deactivateUser(User user);    // 휴면 처리: UserStatus=INACTIVE
    void activateUser(User user);      // 휴면 해제: UserStatus=ACTIVE

    // 삭제 관련
    void deleteUser(User user);         // 소프트 삭제: RecordStatus=DELETED + UserStatus=DELETED
    void restoreUser(User user);        // 복구: RecordStatus=ACTIVE + UserStatus=ACTIVE
    void hardDeleteUser(User user);     // 컬렉션/DB에서 완전 제거
}
