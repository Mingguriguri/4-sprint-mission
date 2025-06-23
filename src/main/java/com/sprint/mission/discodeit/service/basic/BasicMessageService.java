package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    @Qualifier("JCFMessageRepository")
    private final MessageRepository messageRepository;

    @Qualifier("JCFChannelRepository")
    private final ChannelRepository channelRepository;

    @Qualifier("JCFUserRepository")
    private final UserRepository userRepository;

    @Qualifier("JCFBinaryContentRepository")
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponseDto create(MessageCreateDto createMessageDto) {
        if (!channelRepository.existsById(createMessageDto.getChannelId())) {
            throw new NoSuchElementException("Channel not found with id " + createMessageDto.getChannelId());
        }
        if (!userRepository.existsById(createMessageDto.getAuthorId())) {
            throw new NoSuchElementException("Author not found with id " + createMessageDto.getAuthorId());
        }

        List<UUID> attachments = createMessageDto.getAttachmentIds() != null
                ? createMessageDto.getAttachmentIds()
                : new ArrayList<>();

        Message message = new Message(
                createMessageDto.getContent(),
                createMessageDto.getChannelId(),
                createMessageDto.getAuthorId(),
                attachments
        );
        messageRepository.save(message);
        return MessageResponseDto.from(message);
    }

    @Override
    public MessageResponseDto find(UUID messageId) {
        Message message =  messageRepository.findById(messageId)
                .orElseThrow(() -> new NoSuchElementException("Message with id " + messageId + " not found"));

        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        List<Message> messageList = messageRepository.findAllByChannelID(channelId);
        return messageList.stream()
                .map(MessageResponseDto::from).toList();
    }

    @Override
    public MessageResponseDto update(MessageUpdateDto updateMessageDto) {
        Message message = messageRepository.findById(updateMessageDto.getId())
                .orElseThrow(() -> new NoSuchElementException("Message with id " + updateMessageDto.getId() + " not found"));
        message.updateContent(updateMessageDto.getContent());
        message.changeAttachmentIds(updateMessageDto.getAttachmentIds());
        message.touch();
        messageRepository.save(message);
        return MessageResponseDto.from(message);
    }

    @Override
    public void delete(UUID messageId) {
        if (!messageRepository.existsById(messageId)) {
            throw new NoSuchElementException("Message with id " + messageId + " not found");
        }
        binaryContentRepository.deleteByMessageId(messageId);
        messageRepository.deleteById(messageId);
    }
}
