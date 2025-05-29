package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;

public interface ChannelService {

    List<Channel> getAllChannels();

    Optional<Channel> getChannelById(String id);

    List<Channel> getChannelByName(String channelName);

    Channel createChannel(String channelName, String description);

    Channel updateChannel(String id, String channelName, String description);

    void deleteChannel(String id);

}
