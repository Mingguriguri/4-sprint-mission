package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChannelService {

    List<Channel> getAllChannels();

    Optional<Channel> getChannelById(String id);

    List<Channel> getChannelByName(String channelName);

    Channel createChannel(String channelName, String description, Set<String> memberIds, String ownerId);

    Channel updateChannel(String id, String channelName, String description, Set<String> memberIds, String ownerId);       // 채널 전체 수정
    Channel updateChannelInfo(String id, String channelName, String description);   // 채널 기본 정보 수정
    Channel updateChannelMembers(String id, Set<String> memberIds);                 // 채널 멤버 수정
    Channel updateChannelOwner(String id, String ownerId);                          // 채널 주인 수정

    void deleteChannel(String id);

}
