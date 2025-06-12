package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public JCFMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAllByRecordStatusIsActive();
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        validateNotNullId(messageId);
        return messageRepository.findById(messageId);
    }

    @Override
    public List<Message> getMessageByUserId(String userId) {
        validateNotNullId(userId);
        return messageRepository.findByUserId(userId);
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        validateNotNullId(channelId);
        return messageRepository.findByChannelId(channelId);
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

        return messageRepository.save(message);
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Message updateMessage(String messageId, Channel channel, User user, String content) {
        validateNotNullId(messageId);
        validateActiveChannel(channel);
        validateActiveUser(user);

        Message targetMessage = messageRepository.findByRecordStatusIsActiveAndId(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found or not ACTIVE"));

        targetMessage.addChannel(channel);
        targetMessage.addUser(user);
        targetMessage.changeMessageContent(content);
        targetMessage.touch();

        return messageRepository.save(targetMessage);
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteMessageById(String messageId) {
        validateNotNullId(messageId);
        messageRepository.softDeleteById(messageId);
    }

    @Override
    public void restoreMessageById(String messageId) {
        validateNotNullId(messageId);
        messageRepository.restoreById(messageId);
    }

    @Override
    public void hardDeleteMessageById(String messageId) {
        validateNotNullId(messageId);
        messageRepository.deleteById(messageId);
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */

    /**
     * 메시지 ID가 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param id 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullId(String id) {
        if (id == null || id.trim().isEmpty()) {
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