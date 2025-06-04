package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface MessageService {

    List<Message> getAllMessages();
    // messageId로 조회
    Optional<Message> getMessageById(String messageId);
    // ACTIVE, DELETE, HARD_DELETE를 구분하여 조회하기 위함
    Optional<Message> getMessageByIdWithStatus(String messageId, RecordStatus recordStatus);
    List<Message> getMessageByUserId(String senderId);
    List<Message> getMessageByChannelId(String channelId);

    Message createMessage(Channel channel, User user, String content);

    Message updateMessage(String messageId,Channel channel, User user, String content);

    void deleteMessageById(String messageId);
    void restoreMessageById(String messageId);
    void hardDeleteMessageById(String messageId);
}
