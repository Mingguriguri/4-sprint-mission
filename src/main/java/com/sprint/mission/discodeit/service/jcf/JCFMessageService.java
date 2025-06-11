package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private final ArrayList<Message> messageList;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageList = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<Message> getAllMessages() {
        return messageList.stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        validateNotNullId(messageId);
        return messageList.stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(msg -> msg.getUser().getId().equals(messageId))
                .findFirst();
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        validateNotNullId(userId);
        return messageList.stream()
                .filter(msg -> msg.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(msg -> msg.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        validateNotNullId(channelId);
        return messageList.stream()
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
        Message message = new Message(channel, user, content);
        channel.addMessage(message);
        user.addMessage(message);
        messageList.add(message);
        return message;

    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Message updateMessage(String messageId, Channel channel, User user, String content) {
        validateNotNullId(messageId);

        Message message = findMessageOrThrow(messageId, RecordStatus.ACTIVE);
        validateActiveChannel(channel);
        validateActiveUser(user);

        message.addChannel(channel);
        message.addUser(user);
        message.sendMessageContent(content);
        message.touch();

        return message;
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteMessageById(String messageId) {
        validateNotNullId(messageId);
        Message message = findMessageOrThrow(messageId, RecordStatus.ACTIVE);
        message.touch();
        message.softDelete();
    }

    @Override
    public void restoreMessageById(String messageId) {
        validateNotNullId(messageId);
        Message message = findMessageOrThrow(messageId, RecordStatus.DELETED);
        message.touch();
        message.restore();
    }

    @Override
    public void hardDeleteMessageById(String messageId) {
        validateNotNullId(messageId);
        Message message = findMessageOrThrow(messageId, RecordStatus.DELETED);
        messageList.remove(message);
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
        return messageList.stream()
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