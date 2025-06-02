package com.sprint.mission.discodeit.entity;

public class Message extends Base{
    private Channel channel;
    private User user;
    private String content;

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
        if (this.channel != null) {
            this.channel = channel;
            channel.addMessage(this);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user = user;
            user.addMessage(this);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + super.getId() +
                ", channelName=" + channel.getChannelName() +
                ", senderName=" + user.getUsername() +
                ", content='" + content + '\'' +
                '}';
    }
}
