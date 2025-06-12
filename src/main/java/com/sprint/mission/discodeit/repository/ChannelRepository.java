package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChannelRepository {
    // CREATE or UPDATE
    Channel save(Channel channel);

    // READ
    Set<Channel> findAllByRecordStatusIsActive();
    Optional<Channel> findById(String id);
    Optional<Channel> findByRecordStatusIsActiveId(String id);

    // 조회 조건
    List<Channel> findByChannelName(String channelName);
    List<Channel> findByUserId(String userId);

    // DELETE
    void softDeleteById(String id);
    void restoreById(String id);
    void deleteById(String id);
}
