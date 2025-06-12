package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JCFChannelRepository implements ChannelRepository {
    private final Set<Channel> channelRepository = new HashSet<>();

    @Override
    public Channel save(Channel channel) {
        channelRepository.removeIf(c -> c.getId().equals(channel.getId()));
        channelRepository.add(channel);
        return channel;
    }

    @Override
    public Set<Channel> findAllByRecordStatusIsActive() {
        return channelRepository.stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Channel> findById(String id) {
        return channelRepository.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Channel> findByRecordStatusIsActiveId(String id) {
        return channelRepository.stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> findByChannelName(String channelName) {
        return channelRepository.stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getId().equals(channelName))
                .toList();
    }

    @Override
    public List<Channel> findByUserId(String userId) {
        return channelRepository.stream()
                .filter(c -> c.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(c -> c.getUsers().stream()
                        .anyMatch(u -> u.getId().equals(userId)))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        findByRecordStatusIsActiveId(id).ifPresent(c -> {
                    c.softDelete();
                    c.touch();
                });
    }

    @Override
    public void restoreById(String id) {
        findById(id).ifPresent(c -> {
                    c.restore();
                    c.touch();
                });
    }

    @Override
    public void deleteById(String id) {
        channelRepository.removeIf(c -> c.getId().equals(id));
    }
}
