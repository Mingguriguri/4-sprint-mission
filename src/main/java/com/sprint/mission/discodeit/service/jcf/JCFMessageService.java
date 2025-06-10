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
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .toList();
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public Optional<Message> getMessageByIdWithStatus(String messageId, RecordStatus recordStatus) {
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        if (recordStatus == null) {
            throw new IllegalArgumentException("RecordStatus cannot be null");
        }
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(recordStatus))
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public List<Message> getMessageByUserId(String senderId) {
        if (senderId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getUser().getId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public Message createMessage(Channel channel, User user, String content) {
        // channel이 null이거나 recordStatus != ACTIVE일 경우
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        if (!channel.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("Channel is not ACTIVE: " + channel.getId());
        }

        // user가 null이거나 recordStatus != ACTIVE일 경우
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("User is not ACTIVE: " + user.getId());
        }

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
        // messageId가 null이거나 존재하지 않거나 recordStatus != ACTIVE인 경우
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);
        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("Message with id " + messageId + " not found or not ACTIVE");
        }
        Message message = optionalMessage.get();

        // channel이 null이거나 recordStatus != ACTIVE인 경우
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        if (!channel.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("Channel is not ACTIVE: " + channel.getId());
        }

        // user가 null이거나 recordStatus != ACTIVE인 경우
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("User is not ACTIVE: " + user.getId());
        }

        message.addChannel(channel);
        message.addUser(user);
        message.sendMessageContent(content);
        message.touch();

        return message;

    }

    /* =========================================================
     * DELETE
     * ========================================================= */

    @Override
    public void deleteMessageById(String messageId) {
        // messageId가 null이거나 존재하지 않는 경우
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        // 이미 recordStatus가 DELETED인 경우
        Optional<Message> deletedCheck = getMessageByIdWithStatus(messageId, RecordStatus.DELETED);
        if (deletedCheck.isPresent()) {
            throw new IllegalArgumentException("Message with id " + messageId + " is already DELETED");
        }

        // ACTIVE 상태인지 확인 -> 삭제
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);
        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("Message with id " + messageId + " not found or not ACTIVE");
        }
        Message message = optionalMessage.get();
        message.touch();
        message.softDelete();
    }

    @Override
    public void restoreMessageById(String messageId) {
        // messageId가 null이거나 존재하지 않는 경우
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        // recordStatus가 ACTIVE인 경우 (이미 복원된 상태)
        Optional<Message> activeCheck = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);
        if (activeCheck.isPresent()) {
            throw new IllegalArgumentException("Message with id " + messageId + " is already ACTIVE");
        }

        // DELETED 상태인지 확인 -> 복원
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.DELETED);
        if (optionalMessage.isEmpty()) {
            throw new IllegalArgumentException("Message with id " + messageId + " not found or not DELETED");
        }
        Message message = optionalMessage.get();
        message.touch();
        message.restore();
    }

    @Override
    public void hardDeleteMessageById(String messageId) {
        // messageId가 null이거나 존재하지 않는 경우
        if (messageId == null) {
            throw new IllegalArgumentException("Message ID cannot be null");
        }
        // recordStatus가 ACTIVE인(Soft Delete되지 않은) 메시지인 경우
        Optional<Message> activeCheck = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);
        if (activeCheck.isPresent()) {
            throw new IllegalArgumentException(
                    "Cannot hard delete message with id " + messageId + " because it is still ACTIVE (soft delete first)");
        }
        // DELETED 상태인지 확인
        Optional<Message> deletedCheck = getMessageByIdWithStatus(messageId, RecordStatus.DELETED);
        if (deletedCheck.isEmpty()) {
            throw new IllegalArgumentException("Message with id " + messageId + " not found or not DELETED");
        }
        // 최종 삭제
        messageList.remove(deletedCheck.get());
    }
}
