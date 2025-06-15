package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;

    public BasicChannelService(ChannelRepository channelRepository, MessageRepository messageRepository) {
        this.channelRepository = channelRepository;
        this.messageRepository = messageRepository;
    }

    /* =========================================================
     * READ
     * ========================================================= */

    @Override
    public Set<Channel> getAllChannels() {
        return channelRepository.findAllByRecordStatusIsActive();
    }

    @Override
    public Optional<Channel> getChannelById(String channelId) {
        validateNotNullChannelField(channelId);
        return channelRepository.findById(channelId);
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        validateNotNullChannelField(channelName);
        return channelRepository.findByChannelName(channelName);
    }

    @Override
    public List<Channel> getChannelByUserId(String userId) {
        validateNotNullChannelField(userId);
        return channelRepository.findByUserId(userId);
    }

    /* =========================================================
     * CREATE
     * ========================================================= */

    @Override
    public Channel createChannel(String channelName, String description, Set<User> users, String ownerId) {
        validateActiveUsers(users);
        validateNotNullChannelField(channelName, ownerId);

        Channel channel = new Channel(channelName, description, users, ownerId);
        return channelRepository.save(channel);
    }

    /* =========================================================
     * UPDATE
     * ========================================================= */

    @Override
    public Channel updateChannelInfo(String channelId, String channelName, String description) {
        validateNotNullChannelField(channelId, channelName);

        Channel targetChannel = channelRepository.findByRecordStatusIsActiveId(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found or not ACTIVE"));
        targetChannel.changeChannelName(channelName);
        targetChannel.updateChannelDesc(description);
        targetChannel.touch();
        return channelRepository.save(targetChannel);
    }

    @Override
    public void joinUser(String channelId, User user) {
        validateNotNullChannelField(channelId);
        validateActiveUser(user);

        Channel targetChannel = channelRepository.findByRecordStatusIsActiveId(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found or not ACTIVE"));

        targetChannel.addUser(user);
        targetChannel.touch();
        channelRepository.save(targetChannel);
    }

    @Override
    public void leaveUser(String channelId, User user) {
        validateNotNullChannelField(channelId);
        validateActiveUser(user);

        Channel targetChannel = channelRepository.findByRecordStatusIsActiveId(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found or not ACTIVE"));

        // user가 해당 채널에 참여 중이 아닌 경우
        boolean isMember = targetChannel.getUsers().stream()
                .anyMatch(u -> u.getId().equals(user.getId()));
        if (!isMember) {
            throw new IllegalArgumentException("User with id " + user.getId() + " is not a member of channel " + channelId);
        }

        targetChannel.removeUser(user);
        targetChannel.touch();
        channelRepository.save(targetChannel);
    }

    @Override
    public Channel updateChannelOwner(String channelId, String ownerId) {
        validateNotNullChannelField(channelId, ownerId);

        Channel targetChannel = channelRepository.findByRecordStatusIsActiveId(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found or not ACTIVE"));

        targetChannel.changeChannelOwnerId(ownerId);
        targetChannel.touch();
        return channelRepository.save(targetChannel);
    }

    /* =========================================================
     * DELETE / RESTORE
     * ========================================================= */

    @Override
    public void softDeleteChannel(Channel channel) {
        validateActiveChannel(channel);

        // 메시지 Soft Delete
        channel.getMessages().forEach(msg -> {
            msg.softDelete();
            msg.touch();
            messageRepository.softDeleteById(msg.getId());
        });

        // 채널 Soft Delete
        channelRepository.softDeleteById(channel.getId());
    }

    @Override
    public void restoreChannel(Channel channel) {
        validateDeletedChannel(channel);

        // 메시지 복원
        channel.getMessages().forEach(msg -> {
            msg.restore();
            msg.touch();
            messageRepository.restoreById(msg.getId());
        });

        // 채널 복원
        channelRepository.restoreById(channel.getId());
    }

    @Override
    public void hardDeleteChannel(Channel channel) {
        validateDeletedChannel(channel);

        // 메시지 관계 제거 - Hard Delete
        new ArrayList<>(channel.getMessages()).forEach(channel::removeMessage);

        // 유저 관계 제거 - Hard Delete
        new ArrayList<>(channel.getUsers()).forEach(channel::removeUser);

        // 채널 제거 Hard Delete
        channelRepository.deleteById(channel.getId());
    }

    /* =========================================================
     * INTERNAL VALIDATION HELPERS
     * ========================================================= */

    /**
     * 채널의 ID 또는 이름이 null인지 검사합니다.
     * 주로 외부에서 전달된 ID 인자의 유효성을 사전에 보장하기 위해 사용합니다.
     *
     * @param values 검사할 문자열
     * @throws IllegalArgumentException channelName이 null인 경우
     */
    private void validateNotNullChannelField(String... values) {
        for (String v : values) {
            if (v == null || v.trim().isEmpty()) {
                throw new IllegalArgumentException("Channel Id or Channel Name cannot be null");
            }
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
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Users set cannot be null or empty");
        }

        for (User u : users) {
            if (u == null) {
                throw new IllegalArgumentException("Users set contains null User");
            }
            if (u.getRecordStatus() != RecordStatus.ACTIVE) {
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
