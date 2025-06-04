package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private final Set<Channel> channelSet;

    public JCFChannelService() {
        this.channelSet = new HashSet<>();
    }

    @Override
    public Set<Channel> getAllChannels() {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .collect(Collectors.toSet());
    }

    @Override
    public List<Channel> getChannelByUserId(String userId) {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel ->
                        channel.getUsers().stream()
                                .anyMatch(user -> user.getId().equals(userId))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Channel> getChannelByIdWithActive(String id) {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    // JCFChannelService 내부에서만 씀
    @Override
    public Optional<Channel> getChannelByIdWithStatus(String id, RecordStatus recordStatus) {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(recordStatus))
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public Channel createChannel(String channelName, String description, Set<User> members, String ownerId) {
        Channel channel = new Channel(channelName, description, members, ownerId);
        channelSet.add(channel);
        System.out.println("Successfully Create Channel, " + channel);

        return channel;
    }

    @Override
    public Channel updateChannelInfo(String id, String channelName, String description) {
        Optional<Channel> optionalChannel = getChannelByIdWithActive(id);
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setChannelName(channelName);
            channel.setDescription(description);
            channel.setUpdatedAt(System.currentTimeMillis());

            return channel;
        } else {
            throw new IllegalArgumentException("Channel with id " + id + "not found");
        }
    }

    @Override
    public void joinUser(String channelId, User user) {
        Optional<Channel> optionalChannel = getChannelByIdWithActive(channelId);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.addUser(user);
            channel.setUpdatedAt(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("Channel with id " + channelId + " not found");
        }
    }

    @Override
    public void leaveUser(String channelId, User user) {
        Optional<Channel> optionalChannel = getChannelByIdWithActive(channelId);
        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.removeUser(user);
            channel.setUpdatedAt(System.currentTimeMillis());
        } else {
            throw new IllegalArgumentException("Channel with id " + channelId + " not found");
        }
    }

    @Override
    public Channel updateChannelOwner(String id, String ownerId) {
        Optional<Channel> optionalChannel = getChannelByIdWithActive(id);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setOwnerId(ownerId);
            channel.setUpdatedAt(System.currentTimeMillis());

            return channel;
        } else {
            throw new IllegalArgumentException("Channel with id " + id + "not found");
        }
    }


    @Override
    public void deleteChannel(Channel channel) {
        Optional<Channel> optionalChannel = getChannelByIdWithActive(channel.getId());
        if (optionalChannel.isPresent()) {
            // 메시지 Soft Delete
            List<Message> copyOfMessages = new ArrayList<>(channel.getMessages());
            copyOfMessages.forEach(msg -> {
                        msg.setRecordStatus(RecordStatus.DELETED);
                        msg.setUpdatedAt(System.currentTimeMillis());
                    });

            // 유저 Soft Delete
//            List<User> copyOfUsers = new ArrayList<>(channel.getUsers());
//            for (User user: copyOfUsers) {
//                // TODO 여기서 채널 상관없이 다 지워져버림 유저가.
////                user.setRecordStatus(RecordStatus.DELETED);
////                user.setUpdatedAt(System.currentTimeMillis());
//                channel.removeUser(user);
//
//            }

            // 채널 Soft Delete
            channel.setRecordStatus(RecordStatus.DELETED);
            channel.setUpdatedAt(System.currentTimeMillis());
        }
        else {
            throw new IllegalArgumentException("Channel with id " + channel.getId() + " not found");
        }
    }

    @Override
    public void restoreChannel(Channel channel) {
        Optional<Channel> optionalMessage = getChannelByIdWithStatus(channel.getId(), RecordStatus.DELETED);

        if (optionalMessage.isPresent()) {
            // 메시지 복원
            List<Message> copyOfMessages = new ArrayList<>(channel.getMessages());
            for (Message msg: copyOfMessages) {
                msg.setRecordStatus(RecordStatus.ACTIVE);
                msg.setUpdatedAt(System.currentTimeMillis());
            }

            // 유저 복원
//            List<User> copyOfUsers = new ArrayList<>(channel.getUsers());
//            for (User user: copyOfUsers) {
//                user.setRecordStatus(RecordStatus.ACTIVE);
//                user.setUpdatedAt(System.currentTimeMillis());
//            }

            // 채널 복원
            channel.setRecordStatus(RecordStatus.ACTIVE);
            channel.setUpdatedAt(System.currentTimeMillis());

        }
        else {
            throw new IllegalArgumentException("Channel with id " + channel.getId() + " not found");
        }
    }

    @Override
    public void hardDeleteChannel(Channel channel) {
        Optional<Channel> optionalChannel = getChannelByIdWithStatus(channel.getId(), RecordStatus.DELETED);
        if (optionalChannel.isPresent()) {
            // 메시지 Hard Delete
            List<Message> copyOfMessages = new ArrayList<>(channel.getMessages());
            copyOfMessages.forEach(channel::removeMessage);

            // 유저 Hard Delete
            List<User> copyOfUsers = new ArrayList<>(channel.getUsers());
            copyOfUsers.forEach(channel::removeUser);

            // 채널 Hard Delete
             channelSet.remove(channel);
        }
        else {
            throw new IllegalArgumentException("Channel with id " + channel.getId() + " not found");
        }
    }
}
