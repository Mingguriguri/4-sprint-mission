package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessages();

    Optional<Message> getMessageById(String messageId);

    List<Message> getMessageByUserId(String senderId);

    List<Message> getMessageByChannelId(String channelId);

    Message createMessage(Channel channel, User user, String content);

    Message updateMessage(String messageId,Channel channel, User user, String content);

    void deleteMessageById(String messageId);
}
