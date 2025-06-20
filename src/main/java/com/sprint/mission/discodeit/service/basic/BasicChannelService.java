package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

    @Override
    public Channel create(ChannelType type, String name, String description) {
        Channel channel = new Channel(type, name, description);
        return channelRepository.save(channel);
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelCreateDto privateChannelRequestDto) {
        Channel channel = new Channel(privateChannelRequestDto);
        // 채널에 참여하는 User의 정보를 받아 User별 ReadStatus 정보를 생성
        ReadStatus readStatus = new ReadStatus(privateChannelRequestDto.getUserId(), channel.getId());
        readStatusRepository.save(readStatus);

        return channelRepository.save(channel);
    }

    @Override
    public Channel createPublicChannel(PublicChannelCreateDto publicChannelRequestDto) {
        Channel channel = new Channel(publicChannelRequestDto);
        return channelRepository.save(channel);
    }

    @Override
    public PrivateChannelResponseDto findPrivateChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        UUID userId = readStatusRepository.findAll().stream()
                .filter(rs -> rs.getChannelId().equals(channelId))
                .map(ReadStatus::getUserId)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("User not found"));

        return new PrivateChannelResponseDto(
                channel.getId(),
                userId,
                channel.getLastMessageSentAt()
        );
    }

    @Override
    public PublicChannelResponseDto findPublicChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        return new PublicChannelResponseDto(
                channel.getId(),
                channel.getName(),
                channel.getDescription(),
                channel.getLastMessageSentAt()
        );
    }

    @Override
    public AllChannelByUserIdResponseDto findAllByUserId(UUID userId) {
        // PUBLIC
        List<Channel> publicChannels = channelRepository.findAllByChannelType(ChannelType.PUBLIC);
        List<PublicChannelResponseDto> publicChannelDtos = publicChannels.stream()
                .map(channel -> {
                    return new PublicChannelResponseDto(
                            channel.getId(),
                            channel.getName(),
                            channel.getDescription(),
                            channel.getLastMessageSentAt()
                    );
                })
                .toList();
        // PRIVATE
        List<Channel> privateChannels = channelRepository.findAllByChannelType(ChannelType.PRIVATE).stream()
                .filter(c -> readStatusRepository.existsByUserId(userId))
                .toList();
        List<PrivateChannelResponseDto> privateChannelDtos = privateChannels.stream()
                .map(channel -> {
                    return new PrivateChannelResponseDto(
                            channel.getId(),
                            userId,
                            channel.getLastMessageSentAt()
                    );
                })
                .toList();

        return new AllChannelByUserIdResponseDto(publicChannelDtos, privateChannelDtos);


    }

    @Override
    public Channel update(PublicChannelUpdateDto updatePublicChannelRequestDto) {
        Channel channel = channelRepository.findById(updatePublicChannelRequestDto.getId())
                .filter(c -> c.getType() == ChannelType.PUBLIC) // PRIVATE는 수정 불가
                .orElseThrow(() -> new NoSuchElementException("Channel with id " + updatePublicChannelRequestDto.getId() + " not found"));

        channel.updateName(updatePublicChannelRequestDto.getName());
        channel.updateDescription(updatePublicChannelRequestDto.getDescription());

        return channelRepository.save(channel);
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
