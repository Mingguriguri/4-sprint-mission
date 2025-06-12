package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileMessageRepository implements MessageRepository {
    private static final String FILE_PATH = "./data/messages.ser";
    private final File storageFile;

    public FileMessageRepository() {
        this.storageFile = new File(FILE_PATH);
        if (!storageFile.exists()) {
            writeMessagesToFile(new ArrayList<>());
        }
        // TODO: 테스트끝나면 아래 코드 지우기 . 현재는 실행할 때마다 새로 초기화하기 위해서 추가함
        writeMessagesToFile(new ArrayList<>());
    }

    /* =========================================================
     * File I/O
     * ========================================================= */

    /*
     * 역직렬화
     * */
    private List<Message> readMessagesFromFile() {
        if (!storageFile.exists() || storageFile.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            return (List<Message>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("메시지 목록 조회 실패", e);
        }
    }

    /*
     * 직렬화
     * */
    private void writeMessagesToFile(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 저장 실패", e);
        }
    }

    @Override
    public Message save(Message message) {
        List<Message> allMessages = readMessagesFromFile();
        allMessages.removeIf(m -> m.getId().equals(message.getId()));
        allMessages.add(message);
        writeMessagesToFile(allMessages);
        return message;
    }

    @Override
    public List<Message> findAllByRecordStatusIsActive() {
        return readMessagesFromFile().stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<Message> findById(String id) {
        return readMessagesFromFile().stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Message> findByRecordStatusIsActiveAndId(String id) {
        return readMessagesFromFile().stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Message> findByRecordStatusIsDeletedAndId(String id) {
        return readMessagesFromFile().stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.DELETED)
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findByChannelId(String channelId) {
        return readMessagesFromFile().stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getChannel().getId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByUserId(String userId) {
        return readMessagesFromFile().stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        List<Message> allMessages = readMessagesFromFile();
        Message deleteMessage = allMessages.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Message not found"));

        deleteMessage.softDelete();
        deleteMessage.touch();
        writeMessagesToFile(allMessages);
    }

    @Override
    public void restoreById(String id) {
        List<Message> allMessages = readMessagesFromFile();
        Message restoreMessage = allMessages.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Message not found"));

        restoreMessage.restore();
        restoreMessage.touch();
        writeMessagesToFile(allMessages);
    }

    @Override
    public void deleteById(String id) {
        List<Message> allMessages = readMessagesFromFile();
        allMessages.removeIf(m -> m.getId().equals(id));
        writeMessagesToFile(allMessages);
    }
}
