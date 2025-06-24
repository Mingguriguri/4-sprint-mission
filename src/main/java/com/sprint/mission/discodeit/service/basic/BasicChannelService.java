package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    @Qualifier("JCFChannelRepository")
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    @Qualifier("JCFMessageRepository")
    private final MessageRepository messageRepository;

    private final ChannelMapper channelMapper;

    @Override
    public ChannelResponseDto createPublicChannel(ChannelCreateDto dto) {
        if (dto.getType() != ChannelType.PUBLIC) {
            throw new IllegalArgumentException("채널 타입은 PUBLIC 이어야 합니다.");
        }
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Public 채널 생성 시 name 이 필요합니다.");
        }

        Channel channel = channelMapper.toEntity(dto);
        channelRepository.save(channel);
        return channelMapper.toDto(channel, null, null);
    }

    @Override
    public ChannelResponseDto createPrivateChannel(ChannelCreateDto dto) {
        if (dto.getType() != ChannelType.PRIVATE) {
            throw new IllegalArgumentException("채널 타입은 PRIVATE 이어야 합니다.");
        }
        if (dto.getUserId() == null || dto.getOtherUserId() == null) {
            throw new IllegalArgumentException("Private 채널 생성 시 userId 와 otherUserId 가 필요합니다.");
        }

        Channel channel = channelMapper.toEntity(dto);
        Channel saved = channelRepository.save(channel);

        // ReadStatus 양 쪽 유저
        readStatusRepository.save(new ReadStatus(dto.getUserId(), saved.getId()));
        readStatusRepository.save(new ReadStatus(dto.getOtherUserId(), saved.getId()));

        return channelMapper.toDto(saved, dto.getUserId(), dto.getOtherUserId());
    }

    @Override
    public ChannelResponseDto find(UUID channelId, UUID userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
        if (channel.getType() == ChannelType.PUBLIC) {
            return channelMapper.toDto(channel, null, null);
        } else {
            // PRIVATE 채널의 경우 상대방 ID 조회
            UUID otherId = readStatusRepository.findById(channel.getId()).stream()
                    .map(ReadStatus::getUserId)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Other user not found"));
            return channelMapper.toDto(channel, otherId, userId);
        }
    }

    @Override
    public AllChannelByUserIdResponseDto findAllByUserId(UUID userId) {
        // PUBLIC 채널
        List<ChannelResponseDto> publicDtos = channelRepository
                .findAllByChannelType(ChannelType.PUBLIC)
                .stream()
                .map(channel -> channelMapper.toDto(channel, null, null))
                .toList();

        // PRIVATE 채널 중, 이 userId가 속한 채널만 조회
        List<ChannelResponseDto> privateDtos = readStatusRepository
                .findAllByUserId(userId)
                .stream()
                .map(rs -> {
                    Channel ch = channelRepository.findById(rs.getChannelId())
                            .orElseThrow(() -> new NoSuchElementException("Channel with id " + rs.getChannelId() + " not found"));
                    UUID other = readStatusRepository.findAllByChannelId(ch.getId()).stream()
                            .map(ReadStatus::getUserId)
                            .filter(id -> !id.equals(userId))
                            .findFirst()
                            .orElse(null);
                    return channelMapper.toDto(ch, userId, other);
                })
                .toList();

        return new AllChannelByUserIdResponseDto(publicDtos, privateDtos);
    }

    @Override
    public ChannelResponseDto update(ChannelUpdateDto channelUpdateDto) {
        Channel channel = channelRepository.findById(channelUpdateDto.getId())
                .filter(c -> c.getType() == ChannelType.PUBLIC) // PRIVATE는 수정 불가
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelUpdateDto.getId() + " not found"));

        channelMapper.updateEntity(channelUpdateDto, channel);
        channelRepository.save(channel);
        return channelMapper.toDto(channel, null, null);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteByChannelId(channelId);
        channelRepository.deleteById(channelId);
    }
}
