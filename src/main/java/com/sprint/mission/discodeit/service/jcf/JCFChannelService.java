package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JCFChannelService implements ChannelService {
    private List<Channel> channelList;

    public JCFChannelService() {
        this.channelList = new ArrayList<>();
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelList;
    }

    @Override
    public Optional<Channel> getChannelById(String id) {
        return channelList.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        return channelList.stream()
                .filter(channel -> channel.getChannelName().equals(channelName))
                .collect(Collectors.toList());
    }

    @Override
    public Channel createChannel(String channelName, String description) {
        Channel channel = new Channel(channelName, description);
        channelList.add(channel);
        System.out.println("Successfully Create Channel, " + channel);

        return channel;
    }

    @Override
    public Channel updateChannel(String id, String channelName, String description) {
        Optional<Channel> optionalChannel = getChannelById(id);
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
    public void deleteChannel(String id) {
        boolean removed = channelList.removeIf(channel -> channel.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Channel with id " + id + " not found");
        }
    }
}
