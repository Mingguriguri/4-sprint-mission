package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID id);
    Optional<ReadStatus> findByUserId(UUID userId);
    Optional<ReadStatus> findByChannelId(UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);
    boolean existsByUserId(UUID userId);
    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteById(UUID id);
    void deleteByChannelId(UUID channelId);
}
