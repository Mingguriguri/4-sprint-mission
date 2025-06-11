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
    public Optional<Channel> getChannelById(String channelId) {
        validateNotNullId(channelId);
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        validateNotNullName(channelName);
        return channelSet.stream()
                .filter(channel -> channel.getRecordStatus().equals(RecordStatus.ACTIVE))
                .filter(channel -> channel.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public List<Channel> getChannelByUserId(String userId) {
        validateNotNullId(userId);
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
    public Channel createChannel(String channelName, String description, Set<User> users, String ownerId) {
        validateActiveUsers(users);
        validateNotNullId(ownerId);

        Channel channel = new Channel(channelName, description, users, ownerId);
        channelSet.add(channel);
        System.out.println("Successfully Create Channel, " + channel);

        return channel;
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Channel updateChannelInfo(String channelId, String channelName, String description) {
        validateNotNullId(channelId);

        Optional<Channel> optionalChannel = getChannelById(channelId);
        validateActiveChannel(optionalChannel.get());

        Channel channel = optionalChannel.get();
        channel.changeChannelName(channelName);
        channel.updateChannelDesc(description);
        channel.touch();

        return channel;
    }

    @Override
    public void joinUser(String channelId, User user) {
        validateNotNullId(channelId);

        Optional<Channel> optionalChannel = getChannelById(channelId);
        validateActiveChannel(optionalChannel.get());

        Channel channel = optionalChannel.get();

        // user가 null이거나 recordStatus != ACTIVE인 경우
        validateActiveUser(user);

        channel.addUser(user);
        channel.touch();
    }

    @Override
    public void leaveUser(String channelId, User user) {
        validateNotNullId(channelId);

        Optional<Channel> optionalChannel = getChannelById(channelId);
        validateActiveChannel(optionalChannel.get());
        validateActiveUser(user);

        Channel channel = optionalChannel.get();

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
    public Channel updateChannelOwner(String channelId, String ownerId) {
        validateNotNullId(channelId);
        validateNotNullId(ownerId);

        Optional<Channel> optionalChannel = getChannelById(channelId);
        validateActiveChannel(optionalChannel.get());

        Channel channel = optionalChannel.get();
        channel.changeChannelOwnerId(ownerId);
        channel.touch();

        return channel;
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void deleteChannel(Channel channel) {
        validateActiveChannel(channel);

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
        validateDeletedChannel(channel);

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
        validateDeletedChannel(channel);

        // 메시지 관계 제거 - Hard Delete
        List<Message> copyOfMessages = new ArrayList<>(channel.getMessages());
        copyOfMessages.forEach(channel::removeMessage);

        // 유저 관계 제거 - Hard Delete
        List<User> copyOfUsers = new ArrayList<>(channel.getUsers());
        copyOfUsers.forEach(channel::removeUser);

        // 채널 제거 Hard Delete
         channelSet.remove(channel);
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */
    /**
     * 주어진 channelId RecordStatus에 해당하는 채널을 찾고 반환합니다.
     * 없으면 IllegalArgumentException을 던집니다.
     *
     * @param channelId       조회할 채널의 ID
     * @param expectedStatus  기대하는 채널의 상태 (ACTIVE, DELETED)
     * @return                조건에 맞는 Message 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private Channel findChannelorThrow(String channelId, RecordStatus expectedStatus) {
        return channelSet.stream()
                .filter(channel -> channel.getId().equals(channelId))
                .filter(channel -> channel.getRecordStatus() == expectedStatus)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Channel with id " + channelId +
                                " not found in status: " + expectedStatus));
    }

    /**
     * 채널 ID가 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param id 검사할 ID 문자열
     * @throws IllegalArgumentException ID가 null인 경우
     */
    private void validateNotNullId(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Channel ID cannot be null");
        }
    }

    /**
     * 채널의 이름이 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param channelName 검사할 문자열
     * @throws IllegalArgumentException channelName이 null인 경우
     */
    private void validateNotNullName(String channelName) {
        if (channelName == null) {
            throw new IllegalArgumentException("Channel name cannot be null");
        }
    }

    /**
     * 채널이 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 채널을 수정하거나 삭제할 때 유효한 채널인지 확인하는 데 사용됩니다.
     *
     * @param channel 검사할 Channel 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveChannel(Channel channel) {
        if (channel == null || channel.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("Channel must be ACTIVE and not null");
        }
    }

    /**
     * 채널이 null이거나 DELETED 상태가 아닌 경우 예외를 발생시킵니다.
     * 채널을 복원하거나 완전 삭제할 때 이미 삭제한 채널인지 확인하는 데 사용됩니다.
     *
     * @param channel 검사할 Channel 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateDeletedChannel(Channel channel) {
        if (channel == null || channel.getRecordStatus() != RecordStatus.DELETED) {
            throw new IllegalArgumentException("Channel must be DELETED and not null");
        }
    }


    /**
     * 유저를 담고 있는 Set이 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 채널을 생성할 때 유효한 유저 Set인지 확인하는데 사용합니다.
     *
     * @param users 검사할 User Set 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveUsers(Set<User> users) {
        if (users == null) {
            throw new IllegalArgumentException("Users set cannot be null");
        }
        // members 안에 null이거나 상태가 ACTIVE가 아닌 User가 있으면 예외
        for (User u : users) {
            if (u == null) {
                throw new IllegalArgumentException("Users set contains null User");
            }
            if (!u.getRecordStatus().equals(RecordStatus.ACTIVE)) {
                throw new IllegalArgumentException(
                        "Cannot add user (id=" + u.getId() + ") with recordStatus != ACTIVE");
            }
        }
    }

    /**
     * 유저가 null이거나 ACTIVE 상태가 아닌 경우 예외를 발생시킵니다.
     * 채널을 수정할 때 유효한 사용자여야 함을 보장합니다.
     *
     * @param user 검사할 User 객체
     * @throws IllegalArgumentException 유효하지 않은 경우
     */
    private void validateActiveUser(User user) {
        if (user == null || user.getRecordStatus() != RecordStatus.ACTIVE) {
            throw new IllegalArgumentException("User must be ACTIVE and not null");
        }
    }
}
