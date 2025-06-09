package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * 유저 관련 CRUD(생성·조회·수정·삭제) 기능을 제공하는 서비스 인터페이스.
 * 존재하지 않는 유저를 참조하거나 잘못된 상태로 처리 요청 시에는 IllegalArgumentException을 던진다.
 */
public interface UserService {

    /* =========================================================
     * READ
     * ========================================================= */

    /**
     * 모든 ACTIVE 상태의 유저를 조회한다.
     *
     * @return recordStatus가 ACTIVE인 유저 객체들의 List (순서는 보장되지 않음)
     */
    List<User> getAllUsers();

    /**
     * 주어진 유저 ID에 해당하고, recordStatus가 ACTIVE인 유저를 조회한다.
     *
     * @param id 조회하려는 유저의 ID
     * @return 조건을 만족하는 유저를 Optional로 반환. 존재하지 않으면 Optional.empty()
     * @throws IllegalArgumentException id가 null인 경우
     */
    Optional<User> getUserById(String id);

    /**
     * 주어진 유저 ID와 recordStatus 조건을 만족하는 유저를 조회한다.
     *
     * @param id           조회하려는 유저의 ID
     * @param recordStatus 조회하려는 레코드 상태 (RecordStatus.ACTIVE, RecordStatus.DELETED)
     * @return 주어진 ID와 recordStatus가 일치하는 유저를 Optional로 반환. 없으면 Optional.empty()
     */
    Optional<User> getUserByIdWithStatus(String id, RecordStatus recordStatus);

    /**
     * 주어진 이메일에 해당하고, recordStatus가 ACTIVE인 유저 목록을 조회한다.
     *
     * @param email 조회하려는 유저 이메일 (예: "minjeong@gmail.com")
     * @return 조건을 만족하는 유저들의 List (순서는 보장되지 않음)
     * @throws IllegalArgumentException email이 null인 경우
     */
    List<User> getUserByEmail(String email);

    /**
     * 주어진 username에 해당하고, recordStatus가 ACTIVE인 유저 목록을 조회한다.
     *
     * @param username 조회하려는 유저 이름 (예: "minjeong")
     * @return 조건을 만족하는 유저들의 List (순서는 보장되지 않음)
     * @throws IllegalArgumentException username이 null인 경우
     */
    List<User> getUserByUsername(String username);

    /* =========================================================
     * CREATE
     * ========================================================= */

    /**
     * 새로운 유저를 생성하여 저장한다.
     * <ul>
     *   <li>createdAt, updatedAt 필드를 현재 시각으로 갱신</li>
     *   <li>userStatus와 recordStatus를 ACTIVE로 초기화</li>
     * </ul>
     *
     * @param username 생성할 유저 이름
     * @param email    생성할 유저의 이메일
     * @param password 생성할 유저의 비밀번호
     * @return 생성된 User 객체
     */
    User createUser(String username, String email, String password);

    /* =========================================================
     * UPDATE
     * ========================================================= */

    /**
     * 주어진 유저 ID에 해당하는 유저의 이름, 이메일, 비밀번호를 수정한다.
     * <ul>
     *   <li>존재하지 않거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>UserStatus=WITHDREW 상태인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param id       수정하려는 유저 ID
     * @param username 새 유저 이름
     * @param email    새 유저 이메일
     * @param password 새 유저 비밀번호
     * @return 수정된 User 객체
     * @throws IllegalArgumentException
     *         - id가 null이거나 존재하지 않는 경우
     *         - recordStatus가 ACTIVE가 아닌 경우 (삭제된 유저)
     *         - UserStatus=WITHDREW 상태인 경우
     */
    User updateUserInfo(String id, String username, String email, String password);

    /**
     * 주어진 유저를 비활성화(UserStatus=INACTIVE) 상태로 변경한다.
     * <ul>
     *   <li>user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>userStatus가 이미 INACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param user 비활성화할 User 객체 (recordStatus=ACTIVE여야 함)
     * @throws IllegalArgumentException
     *         - user가 null이거나 recordStatus != ACTIVE인 경우
     *         - userStatus가 이미 INACTIVE인 경우
     */
    void deactivateUser(User user);

    /**
     * 주어진 유저를 활성화(UserStatus=ACTIVE) 상태로 변경한다.
     * <ul>
     *   <li>user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>userStatus가 이미 ACTIVE인 경우 예외</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param user 활성화할 User 객체 (recordStatus=INACTIVE여야 함)
     * @throws IllegalArgumentException
     *  *         - user가 null이거나 recordStatus != ACTIVE인 경우
     *  *         - userStatus가 이미 ACTIVE인 경우
     */
    void activateUser(User user);

    /* =========================================================
     * DELETE
     * ========================================================= */

    /**
     * 유저를 Soft Delete 상태로 변경한다.
     * <ul>
     *   <li>user가 null이거나 recordStatus != ACTIVE인 경우 예외</li>
     *   <li>유저에 속한 메시지들의 recordStatus를 DELETED로 변경</li>
     *   <li>유저에 속한 채널들의 recordStatus를 DELETED로 변경</li>
     *   <li>user의 userStatus를 INACTIVE로 변경</li>
     *   <li>user의 recordStatus를 DELETED로 변경</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param user Soft Delete 대상 User 객체
     * @throws IllegalArgumentException
     *         - user가 null이거나 recordStatus != ACTIVE인 경우
     */
    void deleteUser(User user);

    /**
     * Soft Delete된 유저를 복원한다.
     * <ul>
     *   <li>user가 null이거나 recordStatus != DELETED인 경우 예외</li>
     *   <li>해당 유저의 메시지들의 recordStatus를 ACTIVE로 변경</li>
     *   <li>해당 유저의 채널들의 recordStatus를 ACTIVE로 변경</li>
     *   <li>user의 userStatus를 ACTIVE로 변경</li>
     *   <li>user의 recordStatus를 ACTIVE로 변경</li>
     *   <li>성공 시 updatedAt 필드를 현재 시각으로 갱신</li>
     * </ul>
     *
     * @param user 복원할 User 객체
     * @throws IllegalArgumentException
     *         - user가 null이거나 recordStatus != DELETED인 경우
     */
    void restoreUser(User user);

    /**
     * Soft Delete된 유저를 완전 삭제(Hard Delete)한다.
     * <ul>
     *   <li>user가 null이거나 recordStatus != DELETED인 경우 예외</li>
     *   <li>유저에 속한 메시지들과의 관계를 모두 제거</li>
     *   <li>유저에 속한 채널들과의 관계를 모두 제거</li>
     *   <li>내부 유저 저장소(userList)에서 유저를 완전 삭제</li>
     * </ul>
     *
     * @param user Hard Delete 대상 User 객체
     * @throws IllegalArgumentException
     *         - user가 null이거나 recordStatus != DELETED인 경우
     */
    void hardDeleteUser(User user);
}
