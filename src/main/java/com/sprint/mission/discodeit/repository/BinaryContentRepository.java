package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    List<BinaryContent> saveAll(List<BinaryContent> binaryContents);

    Optional<BinaryContent> findById(UUID id);
    List<BinaryContent> findAllByIdIn(List<UUID> ids);

    List<BinaryContent> findAll();

    boolean existsById(UUID id);
    void deleteById(UUID id);
}
