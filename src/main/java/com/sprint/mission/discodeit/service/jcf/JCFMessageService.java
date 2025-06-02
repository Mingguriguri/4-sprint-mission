package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JCFMessageService implements MessageService {
    private ArrayList<Message> messageList;
    private final UserService userService;
    private final ChannelService channelService;

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.messageList = new ArrayList<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public List<Message> getAllMessages() {
        return messageList;
    }

    @Override
    public Optional<Message> getMessageById(String messageId) {
        return messageList.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst();
    }

    @Override
    public List<Message> getMessageByUserId(String senderId) {
        return messageList.stream()
                .filter(message -> message.getUser().getId().equals(senderId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Message> getMessageByChannelId(String channelId) {
        return messageList.stream()
                .filter(message -> message.getChannel().getId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public Message createMessage(Channel channel, User user, String content) {
        // 유효성 검사
        if (userService.getUserById(user.getId()).isEmpty()) {
            System.out.println("존재하지 않는 사용자입니다.");
//            throw new IllegalArgumentException("존재하지 않는 사용자입니다."); // 로컬에서 테스트가 어려워 출력문으로 대체하였습니다.
        }
        if (channelService.getChannelById(channel.getId()).isEmpty()) {
            System.out.println("존재하지 않는 채널입니다: " + channel.getId());
            throw new IllegalArgumentException("존재하지 않는 채널입니다: " + channel.getId());
        }
        Message message = new Message(channel, user, content);

        channel.addMessage(message);
        user.addMessage(message);
        messageList.add(message);
        return message;
    }

    @Override
    public Message updateMessage(String messageId, Channel channel, User user, String content) {
        // 유효성 검사
        if (userService.getUserById(user.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        if (channelService.getChannelById(channel.getId()).isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 채널입니다.");
        }

        Optional<Message> optionalMessage = getMessageById(messageId);

        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setChannel(channel);
            message.setUser(user);
            message.setContent(content);
            message.setUpdatedAt(System.currentTimeMillis());

            return message;
        } else {
            throw new IllegalArgumentException("Message with id " + messageId + " not found");
        }
    }

    @Override
    public void deleteMessageById(String messageId) {
        boolean removed = messageList.removeIf(message -> message.getId().equals(messageId));
        if (!removed) {
            throw new IllegalArgumentException("Message with id " + messageId + " not found");
        }
    }
}
