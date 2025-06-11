package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileUserService implements UserService {
    private static final String FILE_PATH = "./data/users.ser";

    private final File storageFile;

    public FileUserService() {
        this.storageFile = new File(FILE_PATH);
        if (!storageFile.exists()) {
            writeUserList(new ArrayList<>());
        }
        // TODO: 테스트끝나면 아래 코드 지우기 . 현재는 실행할 때마다 새로 초기화하기 위해서 추가함
        writeUserList(new ArrayList<>());
    }

    /* =========================================================
     * File I/O
     * ========================================================= */

    /*
    * 역직렬화 (파일 -> List<User>)
    * */
    private List<User> readUserList() {
        if (!storageFile.exists() || storageFile.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("유저 목록 조회 실패", e);
        }
    }

    /*
    * 직렬화 (List<User> -> .ser 파일)
    * */
    private void writeUserList(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 목록 저장 실패", e);
        }
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<User> getAllUsers() {
        return readUserList().stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .toList();
    }

    @Override
    public Optional<User> getUserById(String userId) {
        validateNotNullId(userId);
        return readUserList().stream()
//                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<User> getUserByEmail(String email) {
        validateNotNullUserEmailorName(email);
        return readUserList().stream()
                .filter(user -> user.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUserByUsername(String username) {
        validateNotNullUserEmailorName(username);
        return readUserList().stream()
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
        validateNotNullUserEmailorName(password);

        List<User> users = readUserList();

        User newUser = new User(username, email, password);
        users.add(newUser);

        writeUserList(users);
        System.out.println("Successfully Create User, " + newUser);
        return newUser;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public User updateUserInfo(String userId, String username, String email, String password) {
        validateNotNullId(userId);
        List<User> users = readUserList();

        User target = users.stream()
                .filter(u -> u.getId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        validateActiveUser(target);

        // 활동중인 유저만 수정 가능
        findUserOrThrow(target.getId(), UserStatus.ACTIVE);

        target.changeUsername(username);
        target.updateUserEmail(email);
        target.changeUserPassword(password);
        target.touch();

        writeUserList(users);
        return target;
    }

    // 사용자 비활성화
    @Override
    public void deactivateUser(User user) {
        validateActiveUser(user);
        List<User> users = readUserList();

        User target = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 활성화된 유저만 체크
        findUserOrThrow(target.getId(), UserStatus.ACTIVE);

        target.inactivate();
        target.touch();

        writeUserList(users);
    }

    // 비활성화된 사용자 다시 활성화
    @Override
    public void activateUser(User user) {
        validateActiveUser(user);
        List<User> users = readUserList();

        User target = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 기존에 비활성화된 유저만 체크
        findUserOrThrow(target.getId(), UserStatus.INACTIVE);
        target.activate();
        target.touch();

        writeUserList(users);
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteUser(User user) {
        validateActiveUser(user);
        List<User> users = readUserList();

        User target = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));

        // 메시지 Soft Delete
        System.out.println("<<");
        System.out.println(target.getMessages());
        target.getMessages().forEach(msg -> {
            System.out.println("진입>>>>");
            msg.softDelete();
            msg.touch();
            System.out.println(msg.getRecordStatus());
        });

        // 채널 Soft Delete
        target.getChannels().forEach(msg -> {
            msg.softDelete();
            msg.touch();
        });

        // 유저 Soft Delete
        target.inactivate();
        target.softDelete();
        target.touch();

        writeUserList(users);
    }

    @Override
    public void restoreUser(User user) {
        List<User> users = readUserList();
        User target = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        validateDeletedUser(target);

        // 메시지 복원
        target.getMessages().forEach(msg -> {
            msg.restore();
            msg.touch();
        });

        // 채널 복원
        target.getChannels().forEach(msg -> {
            msg.restore();
            msg.touch();
        });

        // 유저 복원
        target.activate();
        target.restore();
        target.touch();

        writeUserList(users);
    }

    @Override
    public void hardDeleteUser(User user) {
//        System.out.println("hardDelete 진입");
        List<User> users = readUserList();
        User target = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        validateDeletedUser(target);
//        System.out.println(target.getRecordStatus());

        // 메시지 관계 모두 제거
        List<Message> copyOfMessages = new ArrayList<>(user.getMessages());
        copyOfMessages.forEach(user::removeMessage);

        // 채널 관계 모두 제거
        List<Channel> copyOfChannels = new ArrayList<>(user.getChannels());
        copyOfChannels.forEach(user::removeChannel);

        // 유저 제거
        users.remove(target);

        writeUserList(users);
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
        List<User> users = readUserList();

        return users.stream()
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
//            System.out.println("HERE");
        }
    }
}
