package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusCreateDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readStsuts.ReadStatusUpdateDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {
    @Qualifier("JCFReadStatusRepository")
    private final ReadStatusRepository readStatusRepository;

    @Qualifier("JCFChannelRepository")
    private final ChannelRepository channelRepository;

    @Qualifier("JCFUserRepository")
    private final UserRepository userRepository;

    private final ReadStatusMapper readStatusMapper;

    @Override
    public ReadStatusResponseDto create(ReadStatusCreateDto readStatusCreateDto) {
        UUID userId = readStatusCreateDto.getUserId();
        UUID channelId = readStatusCreateDto.getChannelId();

        // 관련된 Channel이나 User가 존재하지 않으면 예외
        channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel not found with id " + channelId));
        userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + userId));

        // 같은 Channel과 User와 관련된 객체가 이미 존재하면 예외
        if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
            throw new IllegalArgumentException("User with id " + userId + " and Channel with id " + channelId + " objects already exist");
        }
        ReadStatus readStatus = readStatusMapper.toEntity(readStatusCreateDto);
        readStatusRepository.save(readStatus);
        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public ReadStatusResponseDto find(UUID id) {
        return readStatusRepository.findById(id)
                .map(readStatusMapper::toDto)
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    @Override
    public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
        return readStatusRepository.findAllByUserId(userId).stream()
                .map(readStatusMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReadStatusResponseDto update(ReadStatusUpdateDto readStatusUpdateDto) {
        ReadStatus readStatus = readStatusRepository.findById(readStatusUpdateDto.getId())
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + readStatusUpdateDto.getId() + " not found"));
        readStatusMapper.updateEntity(readStatusUpdateDto, readStatus);
        readStatusRepository.save(readStatus);

        return readStatusMapper.toDto(readStatus);
    }

    @Override
    public void delete(UUID id) {
        if (!readStatusRepository.existsById(id)) {
            throw new NoSuchElementException("ReadStatus with id " + id + " not found");
        }
        readStatusRepository.deleteById(id);
    }
}
