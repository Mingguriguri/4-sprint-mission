package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    @Qualifier("JCFBinaryContentRepository")
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto create(BinaryContentCreateDto profileImageCreateDto) {
        BinaryContent profileImage = new BinaryContent(
                profileImageCreateDto.getBytes(),
                profileImageCreateDto.getType()
        );

        return BinaryContentResponseDto.from(profileImage);
    }

    @Override
    public BinaryContentResponseDto find(UUID id) {
        return binaryContentRepository.findById(id)
                .map(BinaryContentResponseDto::from)
                .orElseThrow(() -> new NoSuchElementException("Binary Content with id " + id + " not found"));
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> idList) {
        return binaryContentRepository.findAllByIdIn(idList).stream()
                .map(BinaryContentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID id) {
        if (!binaryContentRepository.existsById(id)) {
            throw new NoSuchElementException("Binary Content with id " + id + " not found");
        }
        binaryContentRepository.deleteById(id);
    }
}
