package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
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
    public Channel createChannel(String channelName, String description, Set<String> memberIds, String ownerId) {
        Channel channel = new Channel(channelName, description, memberIds, ownerId);
        channelSet.add(channel);
        System.out.println("Successfully Create Channel, " + channel);

        return channel;
    }

    @Override
    public Channel updateChannel(String id, String channelName, String description, Set<String> memberIds, String ownerId) {
        Optional<Channel> optionalChannel = getChannelById(id);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setChannelName(channelName);
            channel.setDescription(description);
            channel.setMemberIds(memberIds);
            channel.setOwnerId(ownerId);
            channel.setUpdatedAt(System.currentTimeMillis());

            return channel;
        } else {
            throw new IllegalArgumentException("Channel with id " + id + "not found");
        }
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
    public Channel updateChannelMembers(String id, Set<String> memberIds) {
        Optional<Channel> optionalChannel = getChannelById(id);

        if (optionalChannel.isPresent()) {
            Channel channel = optionalChannel.get();
            channel.setMemberIds(memberIds);
            channel.setUpdatedAt(System.currentTimeMillis());

            return channel;
        } else {
            throw new IllegalArgumentException("Channel with id " + id + "not found");
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
