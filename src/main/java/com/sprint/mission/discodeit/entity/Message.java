package com.sprint.mission.discodeit.entity;

public class Message extends Base{
    private String channelId;
    private String senderId;
    private String content;

    public Message(String channelId, String senderId, String content) {
        super();
        this.channelId = channelId;
        this.senderId = senderId;
        this.content = content;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
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
                "channelId='" + channelId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
