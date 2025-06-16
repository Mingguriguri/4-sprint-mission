package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "./data/users.ser";
    private final File storageFile;

    public FileUserRepository() {
        this.storageFile = new File(FILE_PATH);
        if (!storageFile.exists()) {
            writeUsersToFile(new ArrayList<>());
        }
        // TODO: 테스트끝나면 아래 코드 지우기 . 현재는 실행할 때마다 새로 초기화하기 위해서 추가함
        writeUsersToFile(new ArrayList<>());
    }

    /* =========================================================
     * File I/O
     * ========================================================= */

    /*
     * 역직렬화
     * */
    private List<User> readUsersFromFile() {
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
     * 직렬화
     * */
    private void writeUsersToFile(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("유저 목록 저장 실패", e);
        }
    }

    @Override
    public User save(User user) {
        List<User> allUsers = readUsersFromFile();
        allUsers.removeIf(u -> u.getId().equals(user.getId()));
        allUsers.add(user);
        writeUsersToFile(allUsers);
        return user;
    }

    @Override
    public List<User> findAllByRecordStatusIsActive() {
        return readUsersFromFile().stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<User> findById(String id) {
        return readUsersFromFile().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByMemberStatusIsActiveAndId(String id) {
        return readUsersFromFile().stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByMemberStatusIsInactiveAndId(String id) {
        return readUsersFromFile().stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getStatus() == UserStatus.INACTIVE)
                .filter(u -> u.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<User> findByEmail(String email) {
        return readUsersFromFile().stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getEmail().equals(email))
                .toList();
    }

    @Override
    public List<User> findByUsername(String username) {
        return readUsersFromFile().stream()
                .filter(u -> u.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(u -> u.getUsername().equals(username))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        List<User> allUser = readUsersFromFile();
        User deleteUser = allUser.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User not found"));
        deleteUser.softDelete();
        deleteUser.touch();
        writeUsersToFile(allUser);
    }

    @Override
    public void restoreById(String id) {
        List<User> allUser = readUsersFromFile();
        User restoreUser = allUser.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("User not found"));
        restoreUser.restore();
        restoreUser.touch();
        writeUsersToFile(allUser);
    }

    @Override
    public void deleteById(String id) {
        List<User> allUser = readUsersFromFile();
        allUser.removeIf(u -> u.getId().equals(id));
        writeUsersToFile(allUser);
    }
}
