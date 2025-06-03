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

    public void addChannel(Channel channel) {
        this.channel = channel;
        if (!channel.getMessages().contains(this)) {
            channel.addMessage(this);
        }
    }

    public void removeChannel(Channel channel) {
        if (channel.getMessages().contains(this)) {
            this.channel = channel;
            if (channel.getMessages() != null) {
                channel.removeMessage(null);
            }
        }
    }

    public User getUser() {
        return user;
    }

    public void addUser(User user) {
        this.user = user;
        if (!user.getMessages().contains(this)) {
            user.addMessage(this);
        }
    }
    public void removeUser(User user) {
        this.user = user;
        if (user.getMessages().contains(this)) {
            user.removeMessage(null);
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
