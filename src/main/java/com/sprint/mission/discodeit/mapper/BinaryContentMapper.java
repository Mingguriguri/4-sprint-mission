package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import jdk.incubator.vector.VectorOperators;
import org.springframework.stereotype.Component;

@Component
public class BinaryContentMapper {
    /**
     * BinaryContentCreateDto → BinaryContent 엔티티 변환
     */
    public BinaryContent toEntity(BinaryContentCreateDto dto) {
        return new BinaryContent(
                dto.getBytes(),
                dto.getType()
        );
    }

    /**
     * BinaryContent →  MessageResponseDto 변환
     */
    public BinaryContentResponseDto toDto(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getBytes(),
                binaryContent.getType(),
                binaryContent.getCreatedAt()
        );
    }
}
