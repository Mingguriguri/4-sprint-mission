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

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public Set<Channel> getAllChannels() {
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<Channel> getChannelById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    // JCFChannelService 내부에서만 씀
    @Override
    public Optional<Channel> getChannelByIdWithStatus(String id, RecordStatus recordStatus) {
        if (id == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        if (recordStatus == null) {
            throw new IllegalArgumentException("RecordStatus cannot be null");
        }
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(recordStatus))
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        if (channelName == null) {
            throw new IllegalArgumentException("Channel name cannot be null");
        }
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getChannelByUserId(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel ->
                        channel.getUsers().stream()
                                .anyMatch(user -> user.getId().equals(userId))
                )
                .collect(Collectors.toList());
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public Channel createChannel(String channelName, String description, Set<User> members, String ownerId) {
        if (members == null) {
            throw new IllegalArgumentException("Members set cannot be null");
        }
        // members 안에 null이거나 상태가 ACTIVE가 아닌 User가 있으면 예외
        for (User u : members) {
            if (u == null) {
                throw new IllegalArgumentException("Members set contains null User");
            }
            if (!u.getRecordStatus().equals(RecordStatus.ACTIVE)) {
                throw new IllegalArgumentException(
                        "Cannot add user (id=" + u.getId() + ") with recordStatus != ACTIVE");
            }
        }
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }

        Channel channel = new Channel(channelName, description, members, ownerId);
        channelSet.add(channel);
        System.out.println("Successfully Create Channel, " + channel);

        return channel;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Channel updateChannelInfo(String id, String channelName, String description) {
        if (id == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }

        Optional<Channel> optionalChannel = getChannelById(id);
        if (optionalChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel with id " + id + " not found or not ACTIVE");
        }

        Channel channel = optionalChannel.get();
        channel.changeChannelName(channelName);
        channel.updateChannelDesc(description);
        channel.touch();

        return channel;
    }

    @Override
    public void joinUser(String channelId, User user) {
        // channelId가 null이거나 존재하지 않는 경우
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        // channel의 recordStatus가 ACTIVE가 아닌 경우
        Optional<Channel> optionalChannel = getChannelById(channelId);
        if (optionalChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel with id " + channelId + " not found or not ACTIVE");
        }
        Channel channel = optionalChannel.get();

        // user가 null이거나 recordStatus != ACTIVE인 경우
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("Cannot add user with recordStatus != ACTIVE: " + user.getId());
        }

        channel.addUser(user);
        channel.touch();
    }

    @Override
    public void leaveUser(String channelId, User user) {
        // channelId가 null이거나 존재하지 않는 경우
        if (channelId == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        // channel의 recordStatus가 ACTIVE가 아닌 경우
        Optional<Channel> optionalChannel = getChannelById(channelId);
        if (optionalChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel with id " + channelId + " not found or not ACTIVE");
        }
        Channel channel = optionalChannel.get();

        // user가 null이거나 recordStatus != ACTIVE인 경우
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (!user.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("Cannot remove user with recordStatus != ACTIVE: " + user.getId());
        }

        // user가 해당 채널에 참여 중이 아닌 경우
        boolean isMember = channel.getUsers().stream()
                        .anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("User with id " + user.getId() + " is not a member of channel " + channelId);
        }

        channel.removeUser(user);
        channel.touch();
    }

    @Override
    public Channel updateChannelOwner(String id, String ownerId) {
        // id가 null이거나 존재하지 않는 경우
        if (id == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
        // recordStatus가 ACTIVE가 아닌 경우 (삭제된 채널)
        Optional<Channel> optionalChannel = getChannelById(id);
        if (optionalChannel.isEmpty()) {
            throw new IllegalArgumentException("Channel with id " + id + " not found or not ACTIVE");
        }

        // ownerId가 null인 경우
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID cannot be null");
        }
        Channel channel = optionalChannel.get();
        channel.changeChannelOwnerId(ownerId);
        channel.touch();

        return channel;
    }

    /* =========================================================
     * DELETE
     * ========================================================= */

    @Override
    public void deleteChannel(Channel channel) {
        // channel이 null이거나 recordStatus != ACTIVE인 경우
        if (channel == null) {
            throw new IllegalArgumentException("Channel object cannot be null");
        }
        if (!channel.getRecordStatus().equals(RecordStatus.ACTIVE)) {
            throw new IllegalArgumentException("Cannot delete channel whose recordStatus is not ACTIVE: " + channel.getId());
        }
        // 메시지 Soft Delete
        channel.getMessages().stream()
                .forEach(msg -> {
                    msg.softDelete();
                    msg.touch();
        });

        // 채널 Soft Delete
        channel.softDelete();
        channel.touch();
    }

    @Override
    public void restoreChannel(Channel channel) {
        // channel이 null이거나 recordStatus != DELETED인 경우
        if (channel == null) {
            throw new IllegalArgumentException("Channel object cannot be null");
        }
        if (!channel.getRecordStatus().equals(RecordStatus.DELETED)) {
            throw new IllegalArgumentException("Cannot restore channel whose recordStatus is not DELETED: " + channel.getId());
        }

        // 메시지 복원
        channel.getMessages().stream()
                .forEach(msg -> {
                    msg.restore();
                    msg.touch();
        });

        // 채널 복원
        channel.restore();
        channel.touch();
    }

    @Override
    public void hardDeleteChannel(Channel channel) {
        if (channel == null) {
            throw new IllegalArgumentException("Channel object cannot be null");
        }
        if (!channel.getRecordStatus().equals(RecordStatus.DELETED)) {
            throw new IllegalArgumentException("Cannot hard delete channel whose recordStatus is not DELETED: " + channel.getId());
        }

        // 메시지 관계 제거 - Hard Delete
        List<Message> copyOfMessages = new ArrayList<>(channel.getMessages());
        copyOfMessages.forEach(channel::removeMessage);

        // 유저 관계 제거 - Hard Delete
        List<User> copyOfUsers = new ArrayList<>(channel.getUsers());
        copyOfUsers.forEach(channel::removeUser);

        // 채널 제거 Hard Delete
         channelSet.remove(channel);
    }
}
