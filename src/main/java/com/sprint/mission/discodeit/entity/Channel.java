package com.sprint.mission.discodeit.entity;

import java.util.Set;

public class Channel extends Base {
    private String channelName;
    private String description;
    private Set<String> memberIds;
    private String ownerId;

    public Channel(String channelName, String description, Set<String> memberIds, String ownerId) {
        super();
        this.channelName = channelName;
        this.description = description;
        this.memberIds = memberIds;
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

    public Set<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<String> memberIds) {
        this.memberIds = memberIds;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", description='" + description + '\'' +
                ", memberIds=" + memberIds +
                ", ownerId='" + ownerId + '\'' +
                '}';
    }
}

