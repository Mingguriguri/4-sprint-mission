package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.RecordStatus;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChannelService {

    Set<Channel> getAllChannels();
    Optional<Channel> getChannelByIdWithActive(String id);
    Optional<Channel> getChannelByIdWithStatus(String id, RecordStatus recordStatus);
    List<Channel> getChannelByName(String channelName);
    List<Channel> getChannelByUserId(String id);

    Channel createChannel(String channelName, String description, Set<User> members, String ownerId);

    Channel updateChannelInfo(String id, String channelName, String description);   // 채널 기본 정보 수정
    void joinUser(String channelId, User user);
    void leaveUser(String channelId, User user);
    Channel updateChannelOwner(String id, String ownerId);                          // 채널 주인 수정

    void deleteChannel(Channel channel);
    void restoreChannel(Channel channel);
    void hardDeleteChannel(Channel channel);
}
