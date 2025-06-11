package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileMessageService implements MessageService {
    private static final String FILE_PATH = "./data/messages.ser";
    private final File storageFile;

//    private final ArrayList<Message> messageList;
    private final UserService userService;
    private final ChannelService channelService;

    public FileMessageService(UserService userService, ChannelService channelService) {
        this.storageFile = new File(FILE_PATH);
        if (!storageFile.exists()) {
            writeMessageList(new ArrayList<>());
        }
        // TODO: 테스트끝나면 아래 코드 지우기 . 현재는 실행할 때마다 새로 초기화하기 위해서 추가함
        writeMessageList(new ArrayList<>());

        this.userService = userService;
        this.channelService = channelService;
    }

//    public FileMessageService(UserService userService, ChannelService channelService) {
////        this.messageList = new ArrayList<>();
//        this.userService = userService;
//        this.channelService = channelService;
//    }

    /* =========================================================
     * File I/O
     * ========================================================= */
    /*
     * 역직렬화 (파일 -> List<Message>)
     * */
    private List<Message> readMesssageList() {
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
     * 직렬화 (List<Message> -> .ser 파일)
     * */
    private void writeMessageList(List<Message> messages) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(messages);
        } catch (IOException e) {
            throw new RuntimeException("메시지 목록 저장 실패", e);
        }
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<Message> getAllMessages() {
        return readMesssageList().stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        validateNotNullId(messageId);
        return readMesssageList().stream()
//                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(msg -> msg.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        validateNotNullId(userId);
        return readMesssageList().stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(msg -> msg.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        validateNotNullId(channelId);
        return readMesssageList().stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(msg -> msg.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public Message createMessage(Channel channel, User user, String content) {
        validateActiveChannel(channel);
        validateActiveUser(user);

        List<Message> messages = readMesssageList();

        Message message = new Message(channel, user, content);
        channel.addMessage(message);
        user.addMessage(message);
        System.out.println("<<<<<<<<<<<< + user add Message" + user.getMessages());
        messages.add(message);

        writeMessageList(messages);

        return message;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Message updateMessage(String messageId, Channel channel, User user, String content) {
        validateNotNullId(messageId);

        List<Message> messages = readMesssageList();
        Message target = messages.stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found."));

        validateActiveChannel(channel);
        validateActiveUser(user);

        target.addChannel(channel);
        target.addUser(user);
        target.sendMessageContent(content);
        target.touch();

        writeMessageList(messages);

        return target;
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteMessageById(String messageId) {
        validateNotNullId(messageId);

        List<Message> messages = readMesssageList();
        Message target = messages.stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found."));

//        Message message = findMessageOrThrow(messageId, RecordStatus.ACTIVE);
        target.touch();
        target.softDelete();

        writeMessageList(messages);
    }

    @Override
    public void restoreMessageById(String messageId) {
        validateNotNullId(messageId);
        List<Message> messages = readMesssageList();
        Message target = messages.stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found."));


//        Message message = findMessageOrThrow(messageId, RecordStatus.DELETED);
        target.touch();
        target.restore();

        writeMessageList(messages);
    }

    @Override
    public void hardDeleteMessageById(String messageId) {
        validateNotNullId(messageId);
        List<Message> messages = readMesssageList();
        Message target = messages.stream()
                .filter(m -> m.getId().equals(messageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Message not found."));

//        Message message = findMessageOrThrow(messageId, RecordStatus.DELETED);
        messages.remove(target);

        writeMessageList(messages);
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */
    /**
     * 주어진 messageId와 RecordStatus에 해당하는 메시지를 찾고 반환합니다.
     * 없으면 IllegalArgumentException을 던집니다.
     *
     * @param messageId       조회할 메시지의 ID
     * @param expectedStatus  기대하는 메시지의 상태 (ACTIVE, DELETED)
     * @return                조건에 맞는 Message 객체
     */
    private Message findMessageOrThrow(String messageId, RecordStatus expectedStatus) {
        return readMesssageList().stream()
                .filter(msg -> msg.getId().equals(messageId))
                .filter(msg -> msg.getRecordStatus() == expectedStatus)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Message with id " + messageId +
                                " not found in status: " + expectedStatus));
    }

    /**
     * 메시지 ID가 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param id 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    /**
     * 채널이 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 메시지를 생성하거나 수정할 때 유효한 채널인지 확인하는 데 사용됩니다.
     *
     * @param channel 검사할 Channel 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveChannel(Channel channel) {
        if (channel == null || channel.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("Channel must be ACTIVE and not null");
        }
    }

    /**
     * 유저가 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 메시지를 생성하거나 수정할 때 유효한 사용자여야 함을 보장합니다.
     *
     * @param user 검사할 User 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveUser(User user) {
        if (user == null || user.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("User must be ACTIVE and not null");
        }
    }
}
