package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ExceptionCode;
import com.sprint.mission.discodeit.exception.FileAccessException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class BinaryContentMapper {
    /**
     * BinaryContentCreateDto → BinaryContent 엔티티 변환
     */
    public BinaryContent toEntity(BinaryContentCreateDto dto) throws IOException {
        try {
            return new BinaryContent(
                    dto.getFile().getBytes(),
                    dto.getType()
            );
        } catch (IOException e) {
            // 트랜잭션시 롤백을 고려해서 RuntimeException을 상속받은 FileAccessException 형태로 예외 전환해서 던지도록 설정했습니다.
            throw new FileAccessException(ExceptionCode.FILE_IO_ERROR);
        }
    }

    /**
     * BinaryContent → BinaryContentResponseDto 변환
     */
    public BinaryContentResponseDto toDto(BinaryContent binaryContent) {
        return new BinaryContentResponseDto(
                binaryContent.getId(),
                binaryContent.getBytes(),
                binaryContent.getType(),
                binaryContent.getCreatedAt()
        );
    }

    /**
     * BinaryContent → BinaryContentResponseDto를 리스트로 변환
     */
    public List<BinaryContentResponseDto> toDtoList(List<BinaryContent> binaryContents) {
        return binaryContents.stream()
                .map(this::toDto)
                .toList();
    }
}
