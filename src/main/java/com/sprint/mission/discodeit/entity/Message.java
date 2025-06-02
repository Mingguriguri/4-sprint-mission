package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Message extends Base{
    private Channel channel;
    private User user;
    private String content;

    private final List<User> userList = new ArrayList<>();
    private final Set<Channel> channelSet = new HashSet<>();

    public Message(Channel channel, User user, String content) {
        super();
        this.channel = channel;
        this.user = user;
        this.content = content;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getUserList() {
        return userList;
    }

    public Set<Channel> getChannelSet() {
        return channelSet;
    }

    // Channel 관련 관계 메서드
    public void addChannel(Channel channel) {
        channelSet.add(channel);
        if (!channel.getMessages().contains(this)) {
            channel.addMessage(this);
        }
    }

    public void removeChannel(Channel channel) {
        channelSet.remove(channel);
        if (channel.getMessages().contains(this)) {
            channel.removeMessage(this);
        }
    }

    // User 관련 관계 메서드
    public void addUser(User user) {
        userList.add(user);
        if (!user.getMessages().contains(this)) {
            user.addMessage(this);
        }
    }

    public void removeUser(User user) {
        userList.remove(user);
        if (user.getMessages().contains(this)) {
            user.removeMessage(this);
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "channel=" + channel +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", userList=" + userList +
                ", channelSet=" + channelSet +
                '}';
    }
}
