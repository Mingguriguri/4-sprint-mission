package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
    // CREATE or UPDATE
    Message save(Message message);

    // READ
    List<Message> findAllByRecordStatusIsActive();
    Optional<Message> findById(String id);
    Optional<Message> findByRecordStatusIsActiveAndId(String id);
    Optional<Message> findByRecordStatusIsDeletedAndId(String id);

    // 조회 조건
    List<Message> findByChannelId(String channelId);
    List<Message> findByUserId(String userId);

    // DELETE
    void softDeleteById(String id);
    void restoreById(String id);
    void deleteById(String id);
}
