package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JCFMessageRepository implements MessageRepository {
    private final List<Message> messageRepository = new ArrayList<>();

    @Override
    public Message save(Message message) {
        messageRepository.removeIf(m -> m.getId().equals(message.getId()));
        messageRepository.add(message);
        return message;
    }

    @Override
    public List<Message> findAllByRecordStatusIsActive() {
        return messageRepository.stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .toList();
    }

    @Override
    public Optional<Message> findById(String id) {
        return messageRepository.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Message> findByRecordStatusIsActiveAndId(String id) {
        return messageRepository.stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Message> findByRecordStatusIsDeletedAndId(String id) {
        return messageRepository.stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.DELETED)
                .filter(m -> m.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Message> findByChannelId(String channelId) {
        return messageRepository.stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getChannel().getId().equals(channelId))
                .toList();
    }

    @Override
    public List<Message> findByUserId(String userId) {
        return messageRepository.stream()
                .filter(m -> m.getRecordStatus() == RecordStatus.ACTIVE)
                .filter(m -> m.getUser().getId().equals(userId))
                .toList();
    }

    @Override
    public void softDeleteById(String id) {
        findByRecordStatusIsActiveAndId(id).ifPresent(m -> {
            m.softDelete();
            m.touch();
        });
    }

    @Override
    public void restoreById(String id) {
        findById(id).ifPresent(m -> {
            m.restore();
            m.touch();
        });
    }

    @Override
    public void deleteById(String id) {
        messageRepository.removeIf(m -> m.getId().equals(id));
    }
}
