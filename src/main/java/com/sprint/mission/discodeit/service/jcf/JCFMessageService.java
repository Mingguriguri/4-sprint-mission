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

    @Override
    public List<Message> getAllMessages() {
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .toList();
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public Optional<Message> getMessageByIdWithStatus(String messageId, RecordStatus recordStatus) {
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(recordStatus))
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public List<Message> getMessageByUserId(String senderId) {
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getUser().getId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        return messageList.stream()
                .filter(message -> message.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public Message createMessage(Channel channel, User user, String content) {
        // 유효성 검사
        if (userService.getUserById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + user.getId());
        }
        if (channelService.getChannelByIdWithActive(channel.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channel.getId());
        }
        Message message = new Message(channel, user, content);

        channel.addMessage(message);
        user.addMessage(message);
        messageList.add(message);
        return message;
    }

    @Override
    public Message updateMessage(String messageId, Channel channel, User user, String content) {
        // 유효성 검사
        if (userService.getUserById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다: " + user.getId());
        }
        if (channelService.getChannelByIdWithActive(channel.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channel.getId());
        }

        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.addChannel(channel);
            message.addUser(user);
            message.setContent(content);
            message.setUpdatedAt(System.currentTimeMillis());

            return message;
        } else {
            throw new IllegalArgumentException("Message with id " + messageId + " not found");
        }
    }

    @Override
    public void deleteMessageById(String messageId) {
        // Soft Delete
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.ACTIVE);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setUpdatedAt(System.currentTimeMillis());
            message.setRecordStatus(RecordStatus.DELETED);
        }
        else {
            throw new IllegalArgumentException("Message with id " + messageId + " not found");
        }
    }

    @Override
    public void restoreMessageById(String messageId) {
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.DELETED);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setUpdatedAt(System.currentTimeMillis());
            message.setRecordStatus(RecordStatus.ACTIVE);
        }
        else {
            throw new IllegalArgumentException("Message with id " + messageId + " not found");
        }
    }

    @Override
    public void hardDeleteMessageById(String messageId) {
        // Hard Delete
        // Soft Delete 된 상태여야지만 Hard Delete 를 할 수 있다.
        Optional<Message> optionalMessage = getMessageByIdWithStatus(messageId, RecordStatus.DELETED);
        if (optionalMessage.isPresent()) {
            messageList.remove(optionalMessage.get());
        }
        else {
            throw new IllegalArgumentException("You should delete first. Message with id " + messageId + " not found in soft delete");
        }
    }
}
