package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Channel extends Base {
    private String channelName;
    private String description;
    private Set<User> users;
    private String ownerId;

    private final List<String> messages;

    public Channel(String channelName, String description, Set<User> users, String ownerId) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.users = users;
        this.ownerId = ownerId;
        this.messages = new ArrayList<>();
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void addUser(User user) {
        users.add(user);
        if (!user.getChannels().contains(this)) {
            user.addChannel(this);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
        if (user.getChannels().contains(this)) {
            user.removeChannel(this);
        }
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}

