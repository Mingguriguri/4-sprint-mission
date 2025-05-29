package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessages();

    Optional<Message> getMessageById(String messageId);

    List<Message> getMessageByUserId(String senderId);

    List<Message> getMessageByChannelId(String channelId);

    Message createMessage(String channelId, String senderId, String content);

    Message updateMessage(String messageId, String channelId, String senderId, String content);

    void deleteMessageById(String messageId);

    void deleteMessagesByUserId(String senderId);

    void deleteMessageByChannelId(String channelId);
}
