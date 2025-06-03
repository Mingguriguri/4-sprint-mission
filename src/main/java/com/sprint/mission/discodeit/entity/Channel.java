package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Channel extends Base {
    private String channelName;
    private String description;
    private Set<User> users;
    private String ownerId;

    private final List<Message> messages = new ArrayList<>();

    public Channel(String channelName, String description, Set<User> users, String ownerId) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.users = users;
        this.ownerId = ownerId;
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

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // User 관련 관계 메서드
    public void addUser(User user) {
        users.add(user);
        if (!user.getChannels().contains(this)) {
            user.addChannel(this);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
        if (user.getChannels() != null) {
            user.removeChannel(this);
        }
    }

    // Message 관련 관계 메서드
    public void addMessage(Message message) {
        if (!messages.contains(message)) {
            messages.add(message);
            if (!message.getChannel().equals(this)) {
                message.addChannel(this);
            }
        }
    }

    public void removeMessage(Message message) {
        if (messages.contains(message)) {
            messages.remove(message);
            if (message.getChannel() != null) {
                message.removeChannel(this);
            }
        }
    }

    @Override
    public String toString() {
        return "Channel{" +
                "id=" + super.getId() +
                ", channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", users=" + users +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}

