package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
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
        return channelSet;
    }

    @Override
    public Optional<Channel> getChannelById(String id) {
        return channelSet.stream()
                .filter(channel -> channel.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Channel> getChannelByName(String channelName) {
        return channelSet.stream()
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
    public void joinUser(String channelId, User user) {
        Optional<Channel> optionalChannel = getChannelById(channelId);

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
        Optional<Channel> optionalChannel = getChannelById(channelId);
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
        Optional<Channel> optionalChannel = getChannelById(id);

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
    public void deleteChannel(String id) {
        boolean removed = channelSet.removeIf(channel -> channel.getId().equals(id));
        if (!removed) {
            throw new IllegalArgumentException("Channel with id " + id + " not found");
        }
    }
}
